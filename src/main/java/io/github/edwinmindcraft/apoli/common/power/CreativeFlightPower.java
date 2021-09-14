package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.LivingEntity;

public class CreativeFlightPower extends PowerFactory<NoConfiguration> {
	public CreativeFlightPower() {
		super(NoConfiguration.CODEC);
	}

	@Override
	public void tick(ConfiguredPower<NoConfiguration, ?> configuration, LivingEntity entity) {
		if (!entity.level.isClientSide) {
			boolean isActive = configuration.isActive(entity);
			boolean hasAbility = this.hasAbility(entity);
			if (isActive && !hasAbility)
				this.grantAbility(entity);
			else if (!isActive && hasAbility)
				this.revokeAbility(entity);
		}
	}

	@Override
	public void onGained(ConfiguredPower<NoConfiguration, ?> configuration, LivingEntity entity) {
		if (!entity.level.isClientSide && configuration.isActive(entity) && !this.hasAbility(entity))
			this.grantAbility(entity);
	}

	@Override
	public void onLost(ConfiguredPower<NoConfiguration, ?> configuration, LivingEntity entity) {
		if (!entity.level.isClientSide && this.hasAbility(entity))
			this.revokeAbility(entity);
	}

	public boolean hasAbility(LivingEntity entity) {
		//return Apoli.POWER_SOURCE.grants((Player) entity, ability);
		// FIXME
		return false;
	}

	public void grantAbility(LivingEntity entity) {
		//FIXME
	}

	public void revokeAbility(LivingEntity entity) {
		//FIXME
	}
}
