package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EntityGlowPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>> {

	public static boolean shouldGlow(Player player, Entity entity) {
		return IPowerContainer.getPowers(player, ModPowers.ENTITY_GLOW.get()).stream().anyMatch(x -> x.getFactory().doesApply(x, entity));
	}

	public EntityGlowPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredEntityCondition.CODEC, "entity_condition"));
	}

	public boolean doesApply(ConfiguredPower<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>, ?> configuration, Entity target) {
		return target instanceof LivingEntity livingEntity && configuration.getConfiguration().value().map(x -> x.check(livingEntity)).orElse(true);
	}
}
