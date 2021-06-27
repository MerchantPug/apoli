package dev.experimental.apoli.api.power;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public interface INightVisionPower<T extends IDynamicFeatureConfiguration> {
	@SuppressWarnings({"rawtypes", "unchecked"})
	static Optional<Float> getNightVisionStrength(LivingEntity player) {
		return ApoliAPI.getPowerContainer(player).getPowers().stream().filter(x -> x.isActive(player) && x.getFactory() instanceof INightVisionPower).map(x -> getValue((ConfiguredPower) x, player)).max(Float::compareTo);
	}

	private static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & INightVisionPower<T>> float getValue(ConfiguredPower<T, F> configuration, LivingEntity player) {
		return configuration.getFactory().getStrength(configuration, player);
	}

	float getStrength(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
