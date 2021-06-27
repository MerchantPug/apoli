package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.ActiveCooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.FireProjectileConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FireProjectilePower extends ActiveCooldownPowerFactory.Simple<FireProjectileConfiguration> {
	public FireProjectilePower() {
		super(FireProjectileConfiguration.CODEC);
	}

	@Override
	protected void execute(ConfiguredPower<FireProjectileConfiguration, ?> configuration, LivingEntity player) {
		configuration.getConfiguration().fireProjectiles(player);
	}
}
