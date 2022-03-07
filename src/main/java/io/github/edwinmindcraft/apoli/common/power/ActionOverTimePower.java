package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOverItemConfiguration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class ActionOverTimePower extends PowerFactory<ActionOverItemConfiguration> {
	public ActionOverTimePower() {
		super(ActionOverItemConfiguration.CODEC);
		this.ticking(true);
	}

	@Override
	public void tick(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, LivingEntity player) {
		AtomicBoolean data = configuration.getPowerData(player, () -> new AtomicBoolean(false));
		ActionOverItemConfiguration config = configuration.getConfiguration();
		if (configuration.isActive(player)) {
			if (!data.get() && config.risingAction() != null)
				config.risingAction().execute(player);
			if (config.entityAction() != null)
				config.entityAction().execute(player);
			data.set(true);
		} else {
			if (data.get() && config.fallingAction() != null)
				config.fallingAction().execute(player);
			data.set(false);
		}
	}

	@Override
	protected int tickInterval(ActionOverItemConfiguration configuration, LivingEntity player) {
		return configuration.interval();
	}

	@Override
	public Tag serialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, LivingEntity player, IPowerContainer container) {
		return ByteTag.valueOf(configuration.getPowerData(container, () -> new AtomicBoolean(false)).get());
	}

	@Override
	public void deserialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, LivingEntity player, IPowerContainer container, Tag tag) {
		AtomicBoolean data = configuration.getPowerData(container, () -> new AtomicBoolean(false));
		data.set(!Objects.equals(tag, ByteTag.ZERO));
	}
}
