package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IVariableIntPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IVariableIntPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class VariableIntPowerFactory<T extends IVariableIntPowerConfiguration> extends PowerFactory<T> implements IVariableIntPower<T> {
	protected VariableIntPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected VariableIntPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected abstract int get(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container);

	protected abstract void set(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container, int value);

	protected int get(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.get(configuration, player, IPowerContainer.get(player).resolve().orElse(null));
	}

	protected void set(ConfiguredPower<T, ?> configuration, LivingEntity player, int value) {
		this.set(configuration, player, IPowerContainer.get(player).resolve().orElse(null), value);
	}

	@Override
	public int assign(ConfiguredPower<T, ?> configuration, LivingEntity player, int value) {
		value = Mth.clamp(value, this.getMinimum(configuration, player), this.getMaximum(configuration, player));
		this.set(configuration, player, value);
		return value;
	}

	@Override
	public int getValue(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.get(configuration, player);
	}

	@Override
	public int getMaximum(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().max();
	}

	@Override
	public int getMinimum(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().min();
	}

	public static abstract class Simple<T extends IVariableIntPowerConfiguration> extends VariableIntPowerFactory<T> {
		protected Simple(Codec<T> codec) {
			super(codec);
		}

		protected Simple(Codec<T> codec, boolean allowConditions) {
			super(codec, allowConditions);
		}

		protected AtomicInteger getCurrentValue(ConfiguredPower<T, ?> configuration, IPowerContainer container) {
			return configuration.getPowerData(container, () -> new AtomicInteger(configuration.getConfiguration().initialValue()));
		}

		@Override
		protected int get(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container) {
			if (container == null)
				return configuration.getConfiguration().initialValue();
			return this.getCurrentValue(configuration, container).get();
		}

		@Override
		protected void set(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container, int value) {
			if (container == null)
				return;
			this.getCurrentValue(configuration, container).set(value);
		}

		@Override
		public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container) {
			return IntTag.valueOf(this.get(configuration, player, container));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container, Tag tag) {
			if (tag instanceof IntTag intTag)
				this.set(configuration, player, container, intTag.getAsInt());
		}
	}
}
