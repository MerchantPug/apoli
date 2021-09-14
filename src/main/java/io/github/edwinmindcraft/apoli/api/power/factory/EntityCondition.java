package io.github.edwinmindcraft.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.IConditionFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class EntityCondition<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<EntityCondition<?>> implements IConditionFactory<T, ConfiguredEntityCondition<T, ?>, EntityCondition<T>> {
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
