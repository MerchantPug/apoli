package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredFluidCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public record ModifyFluidRenderConfiguration(@Nullable ConfiguredBlockCondition<?, ?> blockCondition,
											 @Nullable ConfiguredFluidCondition<?, ?> fluidCondition,
											 Fluid fluid) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyFluidRenderConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(OptionalFuncs.opt(ModifyFluidRenderConfiguration::blockCondition)),
			CalioCodecHelper.optionalField(ConfiguredFluidCondition.CODEC, "fluid_condition").forGetter(OptionalFuncs.opt(ModifyFluidRenderConfiguration::fluidCondition)),
			SerializableDataTypes.FLUID.fieldOf("fluid").forGetter(ModifyFluidRenderConfiguration::fluid)
	).apply(instance, (cbc, cfc, fluid) -> new ModifyFluidRenderConfiguration(cbc.orElse(null), cfc.orElse(null), fluid)));
}
