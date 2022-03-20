package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.apace100.apoli.access.SubmergableEntity;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

public class SubmergedInCondition extends EntityCondition<TagConfiguration<Fluid>> {

	public SubmergedInCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.FLUID_TAG, "fluid"));
	}

	@Override
	public boolean check(TagConfiguration<Fluid> configuration, Entity entity) {
		return configuration.isLoaded() && ((SubmergableEntity)entity).isSubmergedInLoosely(configuration.value());
	}
}
