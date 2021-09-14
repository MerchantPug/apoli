package io.github.edwinmindcraft.apoli.api.power;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface INightVisionPower<T extends IDynamicFeatureConfiguration> {
	@SuppressWarnings({"rawtypes", "unchecked"})
	static Optional<Float> getNightVisionStrength(LivingEntity player) {
		return IPowerContainer.get(player).map(x -> x.getPowers().stream()).orElseGet(Stream::of)
				.filter(x -> x.isActive(player) && x.getFactory() instanceof INightVisionPower)
				.map(x -> getValue((ConfiguredPower) x, player))
				.max(Float::compareTo);
	}

	private static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & INightVisionPower<T>> float getValue(ConfiguredPower<T, F> configuration, LivingEntity player) {
		return configuration.getFactory().getStrength(configuration, player);
	}

	float getStrength(ConfiguredPower<T, ?> configuration, LivingEntity player);
}
