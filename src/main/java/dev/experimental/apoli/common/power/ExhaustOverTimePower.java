package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ExhaustOverTimeConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ExhaustOverTimePower extends PowerFactory<ExhaustOverTimeConfiguration> {
	public ExhaustOverTimePower() {
		super(ExhaustOverTimeConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(ExhaustOverTimeConfiguration configuration, LivingEntity player) {
		if (player instanceof Player p)
			p.causeFoodExhaustion(configuration.exhaustion());
	}

	@Override
	protected int tickInterval(ExhaustOverTimeConfiguration configuration, LivingEntity player) {
		return configuration.interval();
	}
}
