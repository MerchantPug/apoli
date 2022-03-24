package io.github.edwinmindcraft.apoli.common.condition.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.world.entity.Entity;

import java.util.Objects;
import java.util.function.BiPredicate;

public class SimpleBiEntityCondition extends BiEntityCondition<NoConfiguration> {
	public static boolean ridingRecursive(Entity actor, Entity target) {
		if (actor.getVehicle() == null)
			return false;
		Entity vehicle = actor.getVehicle();
		while (vehicle != target && vehicle != null)
			vehicle = vehicle.getVehicle();
		return Objects.equals(vehicle, target);
	}

	private final BiPredicate<Entity, Entity> predicate;

	public SimpleBiEntityCondition(BiPredicate<Entity, Entity> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	protected boolean check(NoConfiguration configuration, Entity actor, Entity target) {
		return this.predicate.test(actor, target);
	}
}
