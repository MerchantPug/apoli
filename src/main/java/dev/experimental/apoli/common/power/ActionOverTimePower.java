package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ActionOverItemConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
			if (config.entityAction() != null)
				config.entityAction().execute(player);
			if (!data.get() && config.risingAction() != null)
				config.risingAction().execute(player);
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
	public NbtElement serialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, LivingEntity player) {
		return NbtByte.of(configuration.getPowerData(player, () -> new AtomicBoolean(false)).get());
	}

	@Override
	public void deserialize(ConfiguredPower<ActionOverItemConfiguration, ?> configuration, LivingEntity player, NbtElement tag) {
		AtomicBoolean data = configuration.getPowerData(player, () -> new AtomicBoolean(false));
		data.set(!Objects.equals(tag, NbtByte.ZERO));
	}
}
