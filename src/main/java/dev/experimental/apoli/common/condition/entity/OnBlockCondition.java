package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;

public class OnBlockCondition extends EntityCondition<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>> {

	public OnBlockCondition() {
		super(FieldConfiguration.optionalCodec(ConfiguredBlockCondition.CODEC, "block_condition"));
	}

	@Override
	public boolean check(FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>> configuration, LivingEntity entity) {
		return entity.isOnGround() && configuration.value().map(x -> x.check(new CachedBlockPosition(entity.world, entity.getBlockPos(), true))).orElse(true);
	}
}
