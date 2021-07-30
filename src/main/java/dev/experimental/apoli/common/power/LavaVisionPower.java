package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.LavaVisionConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import java.util.Optional;
import net.minecraft.world.entity.Entity;

public class LavaVisionPower extends PowerFactory<LavaVisionConfiguration> {
	public static Optional<Float> getS(Entity entity) {
		return IPowerContainer.getPowers(entity, ModPowers.LAVA_VISION.get()).stream().map(ConfiguredFactory::getConfiguration).map(LavaVisionConfiguration::s).findFirst();
	}

	public static Optional<Float> getV(Entity entity) {
		return IPowerContainer.getPowers(entity, ModPowers.LAVA_VISION.get()).stream().map(ConfiguredFactory::getConfiguration).map(LavaVisionConfiguration::s).findFirst();
	}

	public LavaVisionPower() {
		super(LavaVisionConfiguration.CODEC);
	}
}
