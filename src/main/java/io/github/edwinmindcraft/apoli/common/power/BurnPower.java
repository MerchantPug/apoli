package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.BurnConfiguration;
import net.minecraft.world.entity.Entity;

public class BurnPower extends PowerFactory<BurnConfiguration> {
	public BurnPower() {
		super(BurnConfiguration.CODEC);
		this.ticking();
	}

	@Override
	protected void tick(BurnConfiguration configuration, Entity player) {
		player.setSecondsOnFire(configuration.duration());
	}

	@Override
	protected int tickInterval(BurnConfiguration configuration, Entity player) {
		return configuration.interval();
	}
}
