package dev.experimental.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.architectury.core.RegistryEntry;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.IConditionFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.entity.LivingEntity;

public abstract class EntityCondition<T extends IDynamicFeatureConfiguration> extends RegistryEntry<EntityCondition<?>> implements IConditionFactory<T, ConfiguredEntityCondition<T, ?>, EntityCondition<T>> {
	public static final Codec<EntityCondition<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.ENTITY_CONDITION);
	private final Codec<Pair<T, ConditionData>> codec;

	protected EntityCondition(Codec<T> codec) {
		this.codec = IConditionFactory.conditionCodec(codec);
	}

	@Override
	public Codec<Pair<T, ConditionData>> getConditionCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredEntityCondition<T, ?> configure(T input, ConditionData data) {
		return new ConfiguredEntityCondition<>(this, input, data);
	}

	public boolean check(T configuration, LivingEntity entity) {
		return false;
	}

	public boolean check(T configuration, ConditionData data, LivingEntity entity) {
		return data.inverted() ^ this.check(configuration, entity);
	}
}
