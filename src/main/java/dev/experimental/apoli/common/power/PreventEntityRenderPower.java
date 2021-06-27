package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class PreventEntityRenderPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>> {

	public static boolean isRenderPrevented(Entity entity, Entity target) {
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_ENTITY_RENDER.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, target));
	}

	public PreventEntityRenderPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredEntityCondition.CODEC, "entity_condition"));
	}

	public boolean doesPrevent(ConfiguredPower<FieldConfiguration<Optional<ConfiguredEntityCondition<?, ?>>>, ?> configuration, Entity e) {
		return e instanceof LivingEntity le && configuration.getConfiguration().value().map(x -> x.check(le)).orElse(true);
	}
}
