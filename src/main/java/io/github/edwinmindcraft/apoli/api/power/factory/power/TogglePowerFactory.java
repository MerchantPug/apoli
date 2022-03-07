package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ITogglePowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TogglePowerFactory<T extends ITogglePowerConfiguration> extends PowerFactory<T> implements IActivePower<T> {
	protected TogglePowerFactory(Codec<T> codec) {
		super(codec);
	}

	protected TogglePowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	protected void setStatus(ConfiguredPower<T, ?> configuration, LivingEntity entity, boolean status) {
		this.setStatus(configuration, entity, IPowerContainer.get(entity).resolve().orElse(null), status);
	}

	protected boolean getStatus(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.getStatus(configuration, entity, IPowerContainer.get(entity).resolve().orElse(null));
	}

	protected abstract void setStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container, boolean status);

	protected abstract boolean getStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, @Nullable IPowerContainer container);

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

		private AtomicBoolean getData(ConfiguredPower<T, ?> configuration, IPowerContainer container) {
			return configuration.getPowerData(container, () -> new AtomicBoolean(configuration.getConfiguration().defaultState()));
		}

		@Override
		protected void setStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container, boolean status) {
			if (container == null)
				return;
			this.getData(configuration, container).set(status);
		}

		@Override
		protected boolean getStatus(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container) {
			if (container == null)
				return false;
			return this.getData(configuration, container).get();
		}

		@Override
		public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container) {
			return ByteTag.valueOf(this.getStatus(configuration, player, container));
		}

		@Override
		public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity player, IPowerContainer container, Tag tag) {
			if (tag instanceof ByteTag byteTag)
				this.setStatus(configuration, player, container, byteTag.getAsByte() != 0);
		}
	}
}
