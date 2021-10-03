package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ICooldownPower;
import io.github.edwinmindcraft.apoli.api.power.IHudRenderedPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

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
		this.setLastUseTime(configuration, player, player.getCommandSenderWorld().getGameTime());
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
		return Math.max(player.getCommandSenderWorld().getGameTime() - this.getLastUseTime(configuration, player), 0);
	}

	protected long getRemainingDuration(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return Math.max(this.getLastUseTime(configuration, player) + configuration.getConfiguration().duration() - player.getCommandSenderWorld().getGameTime(), 0);
	}

	@Override
	public int assign(ConfiguredPower<T, ?> configuration, LivingEntity player, int value) {
		value = Mth.clamp(value, this.getMaximum(configuration, player), this.getMaximum(configuration, player));
		this.setLastUseTime(configuration, player, player.getCommandSenderWorld().getGameTime() - value);
		return value;
	}

	@Override
	public int getValue(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return Math.toIntExact(Mth.clamp(this.getRemainingDuration(configuration, player), this.getMaximum(configuration, player), this.getMaximum(configuration, player)));
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
			return configuration.getPowerData(player, () -> new AtomicLong(0));
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
		public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return LongTag.valueOf(this.getLastUseTime(configuration, player));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, Tag tag) {
			if (tag instanceof LongTag longTag)
				this.setLastUseTime(configuration, player, longTag.getAsLong());
		}
	}
}
