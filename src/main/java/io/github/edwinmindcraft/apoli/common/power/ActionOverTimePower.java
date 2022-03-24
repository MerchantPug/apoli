package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOverItemConfiguration;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionOverTimePower extends PowerFactory<ActionOverItemConfiguration> {
	public ActionOverTimePower() {
		super(ActionOverItemConfiguration.CODEC);
		this.ticking(true);
	}

	@Override
	public void tick(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, Entity player) {
		//TODO Start ticking after the power is added.
		AtomicBoolean data = configuration.getPowerData(player, () -> new AtomicBoolean(false));
		ActionOverItemConfiguration config = configuration.getConfiguration();
		if (configuration.isActive(player)) {
			if (!data.get())
				ConfiguredEntityAction.execute(config.risingAction(), player);
			ConfiguredEntityAction.execute(config.entityAction(), player);
			data.set(true);
		} else {
			if (data.get())
				ConfiguredEntityAction.execute(config.fallingAction(), player);
			data.set(false);
		}
	}

	@Override
	protected int tickInterval(ActionOverItemConfiguration configuration, Entity player) {
		return configuration.interval();
	}

	@Override
	public void serialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, IPowerContainer container, CompoundTag tag) {
		tag.putBoolean("WasActive", configuration.getPowerData(container, () -> new AtomicBoolean(false)).get());
	}

	@Override
	public void deserialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, IPowerContainer container, CompoundTag tag) {
		AtomicBoolean data = configuration.getPowerData(container, () -> new AtomicBoolean(false));

		data.set(!Objects.equals(tag, ByteTag.ZERO));
	}
}
