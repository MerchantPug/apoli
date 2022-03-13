package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.IntComparingBiEntityConfiguration;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntBiFunction;

public class IntComparingBECEntityCondition extends EntityCondition<IntComparingBiEntityConfiguration> {
	public static int ridingRecursive(@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition, Entity entity) {
		int count = 0;
		if (entity.isPassenger()) {
			Entity vehicle = entity.getVehicle();
			while (vehicle != null) {
				if (ConfiguredBiEntityCondition.check(biEntityCondition, entity, vehicle))
					++count;
				vehicle = vehicle.getVehicle();
			}
		}
		return count;
	}

	public static int passenger(@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition, Entity entity) {
		int count = 0;
		if (entity.isVehicle()) {
			for (Entity passenger : entity.getPassengers()) {
				if (ConfiguredBiEntityCondition.check(biEntityCondition, passenger, entity))
					++count;
			}
		}
		return count;
	}

	public static int passengerRecursive(@Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition, Entity entity) {
		int count = 0;
		if (entity.isVehicle())
			count = (int) entity.getPassengers().stream().flatMap(Entity::getSelfAndPassengers).filter(passenger -> ConfiguredBiEntityCondition.check(biEntityCondition, passenger, entity)).count();
		return count;
	}

	private final ToIntBiFunction<ConfiguredBiEntityCondition<?, ?>, Entity> function;

	public IntComparingBECEntityCondition(ToIntBiFunction<ConfiguredBiEntityCondition<?, ?>, Entity> function) {
		super(IntComparingBiEntityConfiguration.CODEC);
		this.function = function;
	}

	@Override
	protected boolean check(IntComparingBiEntityConfiguration configuration, Entity entity) {
		int check = this.function.applyAsInt(configuration.biEntityCondition(), entity);
		return configuration.comparison().check(check);
	}
}
