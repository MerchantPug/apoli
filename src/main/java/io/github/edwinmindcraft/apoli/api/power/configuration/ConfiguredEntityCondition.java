package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;

public final class ConfiguredEntityCondition<C extends IDynamicFeatureConfiguration, F extends EntityCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredEntityCondition<?, ?>> CODEC = EntityCondition.CODEC.dispatch(ConfiguredEntityCondition::getFactory, EntityCondition::getConditionCodec);

	public static boolean check(@Nullable ConfiguredEntityCondition<?, ?> condition, Entity entity) {
		return condition == null || condition.check(entity);
	}

	public ConfiguredEntityCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(Entity entity) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), entity);
	}

	@Override
	public String toString() {
		return "CEC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}