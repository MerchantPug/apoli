package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.EntityGlowConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

import java.util.Optional;
import java.util.stream.Stream;

public class EntityGlowPower extends PowerFactory<EntityGlowConfiguration> {

	private static Stream<Holder<ConfiguredPower<EntityGlowConfiguration, EntityGlowPower>>> getGlowPowers(Entity actor, Entity target) {
		return Stream.concat(
				IPowerContainer.getPowers(actor, ApoliPowers.ENTITY_GLOW.get()).stream(),
				IPowerContainer.getPowers(target, ApoliPowers.SELF_GLOW.get()).stream()
		);
	}

	public static boolean shouldGlow(Entity actor, Entity target) {
		return getGlowPowers(actor, target).anyMatch(x -> x.value().getFactory().doesApply(x.value().getConfiguration(), actor, target));
	}

	public static Optional<ColorConfiguration> getGlowColor(Entity actor, Entity target) {
		return getGlowPowers(actor, target).flatMap(x -> x.value().getFactory().getColor(x.value().getConfiguration(), actor, target).stream()).findFirst();
	}

	private final boolean targetSelf;

	public EntityGlowPower(boolean targetSelf) {
		super(EntityGlowConfiguration.CODEC);
		this.targetSelf = targetSelf;
	}

	public boolean doesApply(EntityGlowConfiguration configuration, Entity actor, Entity target) {
		return configuration.applyChecks(actor, target, this.targetSelf);
	}

	public Optional<ColorConfiguration> getColor(EntityGlowConfiguration configuration, Entity actor, Entity target) {
		if (this.doesApply(configuration, actor, target) && !configuration.useTeams())
			return Optional.of(configuration.color());
		return Optional.empty();
	}
}
