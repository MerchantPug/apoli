package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.LavaVisionConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class LavaVisionPower extends PowerFactory<LavaVisionConfiguration> {
	public static Optional<Float> getS(Entity entity) {
		return IPowerContainer.getPowers(entity, ApoliPowers.LAVA_VISION.get()).stream().map(ConfiguredFactory::getConfiguration).map(LavaVisionConfiguration::s).findFirst();
	}

	public static Optional<Float> getV(Entity entity) {
		return IPowerContainer.getPowers(entity, ApoliPowers.LAVA_VISION.get()).stream().map(ConfiguredFactory::getConfiguration).map(LavaVisionConfiguration::v).findFirst();
	}

	public LavaVisionPower() {
		super(LavaVisionConfiguration.CODEC);
	}
}
