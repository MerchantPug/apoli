package dev.experimental.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.ICooldownPower;
import dev.experimental.apoli.api.power.IHudRenderedPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.atomic.AtomicLong;

public abstract class CooldownPowerFactory<T extends ICooldownPowerConfiguration> extends PowerFactory<T> implements ICooldownPower<T>, IHudRenderedPower<T> {
	protected CooldownPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected CooldownPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	@Override
	public void use(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		this.setLastUseTime(configuration, player, player.getEntityWorld().getTime());
		IPowerContainer.sync(player);
	}

	@Override
	public boolean canUse(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.getRemainingDuration(configuration, player) <= 0 && configuration.isActive(player);
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
		return this.getRemainingDuration(configuration, player) > 0;
	}

	protected abstract long getLastUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player);

	protected abstract void setLastUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player, long value);

	protected long getElapsedDuration(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return Math.max(player.getEntityWorld().getTime() - this.getLastUseTime(configuration, player), 0);
	}

	protected long getRemainingDuration(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return Math.max(this.getLastUseTime(configuration, player) - player.getEntityWorld().getTime(), 0);
	}

	@Override
	public int assign(ConfiguredPower<T, ?> configuration, LivingEntity player, int value) {
		value = MathHelper.clamp(value, this.getMaximum(configuration, player), this.getMaximum(configuration, player));
		this.setLastUseTime(configuration, player, player.getEntityWorld().getTime() - value);
		return value;
	}

	@Override
	public int getValue(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return Math.toIntExact(MathHelper.clamp(this.getRemainingDuration(configuration, player), this.getMaximum(configuration, player), this.getMaximum(configuration, player)));
	}

	@Override
	public int getMaximum(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().duration();
	}

	@Override
	public int getMinimum(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return 0;
	}

	/**
	 * A partial implementation of {@link CooldownPowerFactory} with default serialization functions.
	 */
	public static abstract class Simple<T extends ICooldownPowerConfiguration> extends CooldownPowerFactory<T> {
		protected Simple(Codec<T> codec) {
			super(codec);
		}

		protected Simple(Codec<T> codec, boolean allowConditions) {
			super(codec, allowConditions);
		}

		protected AtomicLong getUseTime(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return configuration.getPowerData(player, () -> new AtomicLong(Integer.MIN_VALUE));
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
