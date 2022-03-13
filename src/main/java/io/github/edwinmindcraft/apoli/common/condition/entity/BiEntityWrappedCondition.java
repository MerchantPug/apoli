package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.power.configuration.BiEntityConditionConfiguration;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public class BiEntityWrappedCondition extends EntityCondition<BiEntityConditionConfiguration> {
	public static boolean riding(@Nullable ConfiguredBiEntityCondition<?, ?> configuration, Entity entity) {
		return entity.isPassenger() && ConfiguredBiEntityCondition.check(configuration, entity, entity.getVehicle());
	}

	public static boolean ridingRoot(@Nullable ConfiguredBiEntityCondition<?, ?> configuration, Entity entity) {
		return entity.isPassenger() && ConfiguredBiEntityCondition.check(configuration, entity, entity.getRootVehicle());
	}

	private final BiPredicate<ConfiguredBiEntityCondition<?, ?>, Entity> predicate;

	public BiEntityWrappedCondition(BiPredicate<ConfiguredBiEntityCondition<?, ?>, Entity> predicate) {
		super(BiEntityConditionConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	protected boolean check(BiEntityConditionConfiguration configuration, Entity entity) {
		return this.predicate.test(configuration.biEntityCondition(), entity);
	}
}
