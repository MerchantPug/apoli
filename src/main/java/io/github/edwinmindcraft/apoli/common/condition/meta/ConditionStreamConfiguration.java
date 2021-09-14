package io.github.edwinmindcraft.apoli.common.condition.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.IStreamConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record ConditionStreamConfiguration<T, V>(List<T> entries, String name, BiPredicate<T, V> predicate,
												 Predicate<Stream<Boolean>> check) implements IDelegatedConditionConfiguration<V>, IStreamConfiguration<T> {

	public static <T, V> Codec<ConditionStreamConfiguration<T, V>> codec(Codec<T> codec, String name, BiPredicate<T, V> predicate, Predicate<Stream<Boolean>> check) {
		return RecordCodecBuilder.create(instance -> instance.group(
				CalioCodecHelper.listOf(codec).fieldOf("conditions").forGetter(ConditionStreamConfiguration::entries)
		).apply(instance, c -> new ConditionStreamConfiguration<>(c, name, predicate, check)));
	}

	public static <T, V> Codec<ConditionStreamConfiguration<T, V>> andCodec(Codec<T> codec, BiPredicate<T, V> predicate) {
		return codec(codec, "And", predicate, x -> x.allMatch(y -> y));
	}

	public static <T, V> Codec<ConditionStreamConfiguration<T, V>> orCodec(Codec<T> codec, BiPredicate<T, V> predicate) {
		return codec(codec, "Or", predicate, x -> x.anyMatch(y -> y));
	}

	@Override
	public boolean check(V parameters) {
		return this.check().test(this.entries().stream().map(x -> this.predicate().test(x, parameters)));
	}
}
