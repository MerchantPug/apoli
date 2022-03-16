package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class ConfiguredEntityCondition<C extends IDynamicFeatureConfiguration, F extends EntityCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredEntityCondition<?, ?>> CODEC = EntityCondition.CODEC.dispatch(ConfiguredEntityCondition::getFactory, EntityCondition::getConditionCodec);
	public static final MapCodec<Optional<ConfiguredEntityCondition<?, ?>>> OPTIONAL_FIELD = CalioCodecHelper.optionalField(CODEC, "entity_condition");

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