package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ExhaustOverTimeConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ExhaustOverTimePower extends PowerFactory<ExhaustOverTimeConfiguration> {
	public ExhaustOverTimePower() {
		super(ExhaustOverTimeConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(ExhaustOverTimeConfiguration configuration, Entity player) {
		if (player instanceof Player p)
			p.causeFoodExhaustion(configuration.exhaustion());
	}

	@Override
	protected int tickInterval(ExhaustOverTimeConfiguration configuration, Entity player) {
		return configuration.interval();
	}
}
