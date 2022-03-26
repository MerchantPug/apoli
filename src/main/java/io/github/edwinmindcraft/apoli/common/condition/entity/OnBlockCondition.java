package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.Optional;

public class OnBlockCondition extends EntityCondition<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>> {

	public OnBlockCondition() {
		super(FieldConfiguration.optionalCodec(ConfiguredBlockCondition.CODEC, "block_condition"));
	}

	@Override
	public boolean check(FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>> configuration, Entity entity) {
		return entity.isOnGround() && ConfiguredBlockCondition.check(configuration.value().orElse(null), entity.level, entity.blockPosition());
	}
}
