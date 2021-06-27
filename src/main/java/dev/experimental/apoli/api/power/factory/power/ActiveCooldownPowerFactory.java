package dev.experimental.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.IActivePower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;

import java.util.concurrent.atomic.AtomicLong;

public abstract class ActiveCooldownPowerFactory<T extends IActiveCooldownPowerConfiguration> extends CooldownPowerFactory<T> implements IActivePower<T> {
	protected ActiveCooldownPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected ActiveCooldownPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected abstract void execute(ConfiguredPower<T, ?> configuration, LivingEntity player);

	@Override
	public void activate(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		if (this.canUse(configuration, player)) {
			this.execute(configuration, player);
			this.use(configuration, player);
		}
	}

	@Override
	public Key getKey(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().key();
	}

	/**
	 * A partial implementation of {@link ActiveCooldownPowerFactory} with default serialization functions.
	 */
	public static abstract class Simple<T extends IActiveCooldownPowerConfiguration> extends ActiveCooldownPowerFactory<T> {
		protected Simple(Codec<T> codec) {
			super(codec);
		}

		protected Simple(Codec<T> codec, boolean allowConditions) {
			super(codec, allowConditions);
		}

		protected AtomicLong getUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return configuration.getPowerData(player, () -> new AtomicLong(Long.MIN_VALUE));
		}

		@Override
		protected long getLastUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return this.getUseTime(configuration, player).get();
		}

		@Override
		protected void setLastUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player, long value) {
			this.getUseTime(configuration, player).set(value);
		}

		@Override
		public NbtElement serialize(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return NbtLong.of(this.getLastUseTime(configuration, player));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, NbtElement tag) {
			if (tag instanceof NbtLong longTag)
				this.setLastUseTime(configuration, player, longTag.longValue());
		}
	}
}
