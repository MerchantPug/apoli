package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class OnBlockCondition extends EntityCondition<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>> {

	public OnBlockCondition() {
		super(FieldConfiguration.optionalCodec(ConfiguredBlockCondition.CODEC, "block_condition"));
	}

	@Override
	public boolean check(FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>> configuration, Entity entity) {
		return entity.isOnGround() && ConfiguredBlockCondition.check(configuration.value().orElse(null), entity.level, new BlockPos(entity.getX(), entity.getBoundingBox().minY - 0.5000001D, entity.getZ()));
	}
}
