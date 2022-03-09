package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;

public abstract class ActiveCooldownPowerFactory<T extends IActiveCooldownPowerConfiguration> extends CooldownPowerFactory<T> implements IActivePower<T> {
	protected ActiveCooldownPowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected ActiveCooldownPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected abstract void execute(ConfiguredPower<T, ?> configuration, Entity player);

	@Override
	public void activate(ConfiguredPower<T, ?> configuration, Entity player) {
		if (this.canUse(configuration, player)) {
			this.execute(configuration, player);
			this.use(configuration, player);
		}
	}

	@Override
	public Key getKey(ConfiguredPower<T, ?> configuration, Entity player) {
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

		protected AtomicLong getUseTime(ConfiguredPower<T, ?> configuration, IPowerContainer container) {
			return configuration.getPowerData(container, () -> new AtomicLong(Long.MIN_VALUE));
		}

		@Override
		protected long getLastUseTime(ConfiguredPower<T, ?> configuration, @Nullable IPowerContainer container) {
			if (container == null)
				return 0;
			return this.getUseTime(configuration, container).get();
		}

		@Override
		protected void setLastUseTime(ConfiguredPower<T, ?> configuration, @Nullable IPowerContainer container, long value) {
			if (container == null) return;
			this.getUseTime(configuration, container).set(value);
		}

		@Override
		public Tag serialize(ConfiguredPower<T, ?> configuration, IPowerContainer container) {
			return LongTag.valueOf(this.getLastUseTime(configuration, container));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, IPowerContainer container, Tag tag) {
			if (tag instanceof LongTag longTag)
				this.setLastUseTime(configuration, container, longTag.getAsLong());
		}
	}
}
