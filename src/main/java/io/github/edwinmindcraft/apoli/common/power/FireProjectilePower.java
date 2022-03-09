package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ActiveCooldownPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.FireProjectileConfiguration;
import net.minecraft.world.entity.Entity;

public class FireProjectilePower extends ActiveCooldownPowerFactory.Simple<FireProjectileConfiguration> {
	public FireProjectilePower() {
		super(FireProjectileConfiguration.CODEC);
	}

	@Override
	protected void execute(ConfiguredPower<FireProjectileConfiguration, ?> configuration, Entity player) {
		configuration.getConfiguration().fireProjectiles(player);
	}
}
