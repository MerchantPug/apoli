package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.HudRenderedVariableIntPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ResourceConfiguration;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ResourcePower extends HudRenderedVariableIntPowerFactory.Simple<ResourceConfiguration> {

	public ResourcePower() {
		super(ResourceConfiguration.CODEC);
	}

	@Override
	public int assign(ConfiguredPower<ResourceConfiguration, ?> configuration, LivingEntity player, int value) {
		int previous = this.get(configuration, player);
		int minimum = this.getMinimum(configuration, player);
		int maximum = this.getMaximum(configuration, player);
		value = Mth.clamp(value, minimum, maximum);
		this.set(configuration, player, value);
		ResourceConfiguration config = configuration.getConfiguration();

		if (previous != value) {
			if (value == minimum) ConfiguredEntityAction.execute(config.minAction(), player);
			if (value == maximum) ConfiguredEntityAction.execute(config.maxAction(), player);
		}
		return value;
	}
}
