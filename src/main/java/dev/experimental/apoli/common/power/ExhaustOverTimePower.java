package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ExhaustOverTimeConfiguration;
import net.minecraft.entity.player.PlayerEntity;

public class ExhaustOverTimePower extends PowerFactory<ExhaustOverTimeConfiguration> {
	public ExhaustOverTimePower() {
		super(ExhaustOverTimeConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(ExhaustOverTimeConfiguration configuration, PlayerEntity player) {
		player.addExhaustion(configuration.exhaustion());
	}

	@Override
	protected int tickInterval(ExhaustOverTimeConfiguration configuration, PlayerEntity player) {
		return configuration.interval();
	}
}
