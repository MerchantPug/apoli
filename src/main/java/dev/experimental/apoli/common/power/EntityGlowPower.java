package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class EntityGlowPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>> {

	public static boolean shouldGlow(PlayerEntity player, Entity entity) {
		return IPowerContainer.getPowers(player, ModPowers.ENTITY_GLOW.get()).stream().anyMatch(x -> x.getFactory().doesApply(x, entity));
	}

	public EntityGlowPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredEntityCondition.CODEC, "entity_condition"));
	}

	public boolean doesApply(ConfiguredPower<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>, ?> configuration, Entity target) {
		return target instanceof LivingEntity livingEntity && configuration.getConfiguration().value().map(x -> x.check(livingEntity)).orElse(true);
	}
}
