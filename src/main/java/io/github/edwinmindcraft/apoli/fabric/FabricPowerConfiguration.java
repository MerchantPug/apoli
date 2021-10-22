package io.github.edwinmindcraft.apoli.fabric;

import com.mojang.serialization.Codec;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;
import java.util.function.Function;

public record FabricPowerConfiguration<P extends Power>(SerializableData.Instance data, BiFunction<PowerType<P>, LivingEntity, P> power) implements IDynamicFeatureConfiguration {
	public static <P extends Power> Codec<FabricPowerConfiguration<P>> codec(SerializableData data, Function<SerializableData.Instance, BiFunction<PowerType<P>, LivingEntity, P>> ctor) {
		return data.xmap(x -> new FabricPowerConfiguration<>(x, ctor.apply(x)), FabricPowerConfiguration::data).codec();
	}
}
