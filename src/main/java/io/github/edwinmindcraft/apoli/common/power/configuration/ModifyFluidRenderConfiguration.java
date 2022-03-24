package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredFluidCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public record ModifyFluidRenderConfiguration(Holder<ConfiguredBlockCondition<?,?>> blockCondition,
											 Holder<ConfiguredFluidCondition<?,?>> fluidCondition,
											 Fluid fluid) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyFluidRenderConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyFluidRenderConfiguration::blockCondition),
			ConfiguredFluidCondition.optional("fluid_condition").forGetter(ModifyFluidRenderConfiguration::fluidCondition),
			SerializableDataTypes.FLUID.fieldOf("fluid").forGetter(ModifyFluidRenderConfiguration::fluid)
	).apply(instance, ModifyFluidRenderConfiguration::new));
}
