package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.DamageCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.damagesource.DamageSource;

public final class ConfiguredDamageCondition<C extends IDynamicFeatureConfiguration, F extends DamageCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredDamageCondition<?, ?>> CODEC = DamageCondition.CODEC.dispatch(ConfiguredDamageCondition::getFactory, Function.identity());

	public static boolean check(@Nullable ConfiguredDamageCondition<?, ?> condition, DamageSource damageSource, float amount) {
		return condition == null || condition.check(damageSource, amount);
	}

	public ConfiguredDamageCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(DamageSource source, float amount) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), source, amount);
	}
}