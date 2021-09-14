package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.IVariableIntPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IVariableIntPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class VariableIntPowerFactory<T extends IVariableIntPowerConfiguration> extends PowerFactory<T> implements IVariableIntPower<T> {
	protected VariableIntPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected VariableIntPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected abstract int get(ConfiguredPower<T, ?> configuration, LivingEntity player);

	protected abstract void set(ConfiguredPower<T, ?> configuration, LivingEntity player, int value);

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

		protected AtomicInteger getCurrentValue(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return configuration.getPowerData(player, () -> new AtomicInteger(configuration.getConfiguration().initialValue()));
		}

		@Override
		protected int get(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return this.getCurrentValue(configuration, player).get();
		}

		@Override
		protected void set(ConfiguredPower<T, ?> configuration, LivingEntity player, int value) {
			this.getCurrentValue(configuration, player).set(value);
		}

		@Override
		public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return IntTag.valueOf(this.get(configuration, player));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, Tag tag) {
			if (tag instanceof IntTag intTag)
				this.set(configuration, player, intTag.getAsInt());
		}
	}
}
