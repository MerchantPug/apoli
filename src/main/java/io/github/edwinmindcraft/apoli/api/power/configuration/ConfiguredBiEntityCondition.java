package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class ConfiguredBiEntityCondition<C extends IDynamicFeatureConfiguration, F extends BiEntityCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredBiEntityCondition<?, ?>> CODEC = BiEntityCondition.CODEC.dispatch(ConfiguredBiEntityCondition::getFactory, BiEntityCondition::getConditionCodec);

	public static boolean check(@Nullable ConfiguredBiEntityCondition<?, ?> condition, Entity actor, Entity target) {
		return condition == null || condition.check(actor, target);
	}

	public ConfiguredBiEntityCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(Entity actor, Entity target) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), actor, target);
	}

	@Override
	public String toString() {
		return "CBEC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}