package dev.experimental.apoli.api.power.factory.power;

import Key;
import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.IActivePower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.ITogglePowerConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public abstract class TogglePowerFactory<T extends ITogglePowerConfiguration> extends PowerFactory<T> implements IActivePower<T> {
	protected TogglePowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected TogglePowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected abstract void setStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, boolean status);

	protected abstract boolean getStatus(ConfiguredPower<T, ?> configuration, LivingEntity player);

	@Override
	public boolean isActive(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return super.isActive(configuration, player) && this.getStatus(configuration, player);
	}

	@Override
	public void activate(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		this.setStatus(configuration, player, !this.getStatus(configuration, player));
		IPowerContainer.sync(player);
	}

	@Override
	public Key getKey(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return configuration.getConfiguration().key();
	}

	public static abstract class Simple<T extends ITogglePowerConfiguration> extends TogglePowerFactory<T> {
		protected Simple(Codec<T> codec) {
			super(codec);
		}

		protected Simple(Codec<T> codec, boolean allowConditions) {
			super(codec, allowConditions);
		}

		@Override
		protected void setStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, boolean status) {
			configuration.getPowerData(player, () -> new AtomicBoolean(configuration.getConfiguration().defaultState())).set(status);
		}

		@Override
		protected boolean getStatus(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return configuration.getPowerData(player, () -> new AtomicBoolean(configuration.getConfiguration().defaultState())).get();
		}

		@Override
		public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity player) {
			return ByteTag.valueOf(this.getStatus(configuration, player));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, Tag tag) {
			if (tag instanceof NbtByte byteTag)
				this.setStatus(configuration, player, byteTag.byteValue() != 0);
		}
	}
}
