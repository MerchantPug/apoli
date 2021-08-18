package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public record StartingInventoryConfiguration(ListConfiguration<Tuple<Integer, ItemStack>> stacks,
											 boolean recurrent) implements IDynamicFeatureConfiguration {
	public static final Codec<StartingInventoryConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfiguration.mapCodec(ApoliDataTypes.POSITIONED_ITEM_STACK, "stack", "stacks").forGetter(StartingInventoryConfiguration::stacks),
			Codec.BOOL.optionalFieldOf("recurrent", false).forGetter(StartingInventoryConfiguration::recurrent)
	).apply(instance, StartingInventoryConfiguration::new));
}
