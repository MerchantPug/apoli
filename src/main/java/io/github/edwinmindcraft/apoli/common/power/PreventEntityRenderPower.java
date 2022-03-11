package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.PreventEntityRenderConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.Entity;

public class PreventEntityRenderPower extends PowerFactory<PreventEntityRenderConfiguration> {

	public static boolean isRenderPrevented(Entity entity, Entity target) {
		return IPowerContainer.getPowers(entity, ApoliPowers.PREVENT_ENTITY_RENDER.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, entity, target));
	}

	public PreventEntityRenderPower() {
		super(PreventEntityRenderConfiguration.CODEC);
	}

	public boolean doesPrevent(ConfiguredPower<PreventEntityRenderConfiguration, ?> configuration, Entity self, Entity target) {
		return ConfiguredEntityCondition.check(configuration.getConfiguration().entityCondition(), target) &&
			   ConfiguredBiEntityCondition.check(configuration.getConfiguration().biEntityCondition(), self, target);
	}
}
