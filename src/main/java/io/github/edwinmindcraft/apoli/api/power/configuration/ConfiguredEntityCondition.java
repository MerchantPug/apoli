package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;

public final class ConfiguredEntityCondition<C extends IDynamicFeatureConfiguration, F extends EntityCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredEntityCondition<?, ?>> CODEC = EntityCondition.CODEC.dispatch(ConfiguredEntityCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredEntityCondition<?, ?> condition, LivingEntity entity) {
		return condition == null || condition.check(entity);
	}

	public ConfiguredEntityCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(LivingEntity entity) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), entity);
	}
}