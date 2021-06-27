package dev.experimental.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.IHudRenderedPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.IHudRenderedVariableIntPowerConfiguration;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class HudRenderedVariableIntPowerFactory<T extends IHudRenderedVariableIntPowerConfiguration> extends VariableIntPowerFactory<T> implements IHudRenderedPower<T> {
	protected HudRenderedVariableIntPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected HudRenderedVariableIntPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	@Override
	public HudRender getRenderSettings(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().hudRender();
	}

	@Override
	public float getFill(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.getProgress(configuration, player);
	}

	@Override
	public boolean shouldRender(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return true;
	}

	public static abstract class Simple<T extends IHudRenderedVariableIntPowerConfiguration> extends HudRenderedVariableIntPowerFactory<T> {
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
		public NbtElement serialize(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return NbtInt.of(this.get(configuration, player));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, NbtElement tag) {
			if (tag instanceof NbtInt intTag)
				this.set(configuration, player, intTag.intValue());
		}
	}
}
