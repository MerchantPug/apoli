package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class InvulnerablePower extends PowerFactory<FieldConfiguration<ConfiguredDamageCondition<?, ?>>> {

	public static boolean isInvulnerableTo(Entity entity, DamageSource source) {
		return IPowerContainer.getPowers(entity, ModPowers.INVULNERABILITY.get()).stream().anyMatch(x -> x.getConfiguration().value().check(source, Float.NaN));
	}

	public InvulnerablePower() {
		super(FieldConfiguration.codec(ConfiguredDamageCondition.CODEC, "damage_condition"));
	}
}
