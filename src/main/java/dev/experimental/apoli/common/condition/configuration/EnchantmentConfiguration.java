package dev.experimental.apoli.common.condition.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

public record EnchantmentConfiguration(IntegerComparisonConfiguration comparison,
									   Enchantment enchantment,
									   Calculation calculation) implements IDynamicFeatureConfiguration {
	public static final Codec<EnchantmentConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IntegerComparisonConfiguration.MAP_CODEC.forGetter(EnchantmentConfiguration::comparison),
			SerializableDataTypes.ENCHANTMENT.fieldOf("enchantment").forGetter(EnchantmentConfiguration::enchantment),
			SerializableDataType.enumValue(Calculation.class).optionalFieldOf("calculation", Calculation.SUM).forGetter(EnchantmentConfiguration::calculation)
	).apply(instance, EnchantmentConfiguration::new));

	@Override
	public @NotNull List<String> getErrors(@NotNull MinecraftServer server) {
		return IDynamicFeatureConfiguration.super.getErrors(server);
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull MinecraftServer server) {
		if (this.enchantment() == null)
			return ImmutableList.of(this.name() + "/Missing Enchantment");
		return ImmutableList.of();
	}

	public boolean applyCheck(Iterable<ItemStack> input) {
		if (this.enchantment() == null)
			return false;
		return this.comparison().check(this.calculation().apply(Streams.stream(input).mapToInt(stack -> EnchantmentHelper.getLevel(this.enchantment(), stack))).orElse(0));
	}

	public boolean applyCheck(ItemStack... stacks) {
		return this.applyCheck(stacks);
	}

	public enum Calculation {
		SUM(x -> x.reduce(Integer::sum)),
		MAX(IntStream::max);

		private final Function<IntStream, OptionalInt> collapse;

		Calculation(Function<IntStream, OptionalInt> collapse) {
			this.collapse = collapse;
		}

		public OptionalInt apply(IntStream stream) {
			return this.collapse.apply(stream);
		}
	}
}
