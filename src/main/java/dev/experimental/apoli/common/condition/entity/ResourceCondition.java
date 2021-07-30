package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.ResourceComparisonConfiguration;
import java.util.OptionalInt;
import net.minecraft.world.entity.LivingEntity;

public class ResourceCondition extends EntityCondition<ResourceComparisonConfiguration> {

	public ResourceCondition() {
		super(ResourceComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(ResourceComparisonConfiguration configuration, LivingEntity entity) {
		IPowerContainer component = ApoliAPI.getPowerContainer(entity);
		ConfiguredPower<?, ?> power = component.getPower(configuration.resource().power());
		if (entity instanceof PlayerEntity player) {
			OptionalInt value = power.getValue(player);
			return value.isPresent() && configuration.comparison().check(value.getAsInt());
		}
		return false;
	}
}
