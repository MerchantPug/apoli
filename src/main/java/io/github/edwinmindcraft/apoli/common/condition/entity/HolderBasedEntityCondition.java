package io.github.edwinmindcraft.apoli.common.condition.entity;

import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.configuration.HolderConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

import java.util.function.BiPredicate;

public class HolderBasedEntityCondition<T> extends EntityCondition<HolderConfiguration<T>> {

	public static <T> HolderBasedEntityCondition<T> required(MapCodec<Holder<T>> codec, BiPredicate<Entity, Holder<T>> predicate) {
		return new HolderBasedEntityCondition<>(codec, true, predicate);
	}

	public static <T> HolderBasedEntityCondition<T> optional(MapCodec<Holder<T>> codec, BiPredicate<Entity, Holder<T>> predicate) {
		return new HolderBasedEntityCondition<>(codec, false, predicate);
	}


	private final BiPredicate<Entity, Holder<T>> predicate;

	public HolderBasedEntityCondition(MapCodec<Holder<T>> codec, boolean required, BiPredicate<Entity, Holder<T>> predicate) {
		super(required ? HolderConfiguration.required(codec) : HolderConfiguration.optional(codec));
		this.predicate = predicate;
	}

	@Override
	public boolean check(HolderConfiguration<T> configuration, Entity entity) {
		return this.predicate.test(entity, configuration.holder());
	}
}
