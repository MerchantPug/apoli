package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public record ModifyBlockRenderConfiguration(Holder<ConfiguredBlockCondition<?,?>> blockCondition,
											 Block block) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyBlockRenderConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.optional("block_condition").forGetter(ModifyBlockRenderConfiguration::blockCondition),
			SerializableDataTypes.BLOCK.fieldOf("block").forGetter(ModifyBlockRenderConfiguration::block)
	).apply(instance, ModifyBlockRenderConfiguration::new));
}
