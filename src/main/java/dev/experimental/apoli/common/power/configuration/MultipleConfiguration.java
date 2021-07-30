package dev.experimental.apoli.common.power.configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.*;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.util.Tuple;

//Validation occurs on every subpower, this should be fine
public record MultipleConfiguration<V>(Map<String, V> children) implements IDynamicFeatureConfiguration {

	public static <V> MapCodec<MultipleConfiguration<V>> mapCodec(Codec<V> codec, Predicate<String> filter) {
		return new MultipleMapCodec<>(codec, filter);
	}

	private static final class MultipleMapCodec<V> extends MapCodec<MultipleConfiguration<V>> {
		private final Codec<V> codec;
		private final Predicate<String> keyFilter;

		private MultipleMapCodec(Codec<V> codec, Predicate<String> keyFilter) {
			this.codec = codec;
			this.keyFilter = keyFilter;
		}

		@Override
		public <T> Stream<T> keys(DynamicOps<T> ops) {
			return ops.compressMaps() ? Stream.of(ops.createString("values")) : Stream.empty();
		}

		@Override
		public <T> DataResult<MultipleConfiguration<V>> decode(DynamicOps<T> ops, MapLike<T> input) {
			DataResult<MapLike<T>> root = ops.compressMaps() ? ops.getMap(input.get("values")) : DataResult.success(input);
			return root.flatMap(map -> {
				ImmutableMap.Builder<String, V> successes = ImmutableMap.builder();
				ImmutableSet.Builder<String> failures = ImmutableSet.builder();
				map.entries().forEach(entry -> {
					ops.getStringValue(entry.getFirst())
							.flatMap(name -> codec.decode(ops, entry.getSecond()).map(x -> new Tuple<>(name, x.getFirst())))
							.resultOrPartial(failures::add)
							.filter(x -> this.keyFilter.test(x.getA()))
							.ifPresent(pair -> successes.put(pair.getA(), pair.getB()));
				});
				ImmutableSet<String> build = failures.build();
				MultipleConfiguration<V> configuration = new MultipleConfiguration<>(successes.build());
				if (!build.isEmpty())
					return DataResult.error("Failed to read fields: " + String.join(", ", build), configuration);
				return DataResult.success(configuration);
			});
		}

		@Override
		public <T> RecordBuilder<T> encode(MultipleConfiguration<V> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
			RecordBuilder<T> root = ops.compressMaps() ? ops.mapBuilder() : prefix;
			input.children().forEach((key, value) -> root.add(key, this.codec.encodeStart(ops, value)));
			if (ops.compressMaps())
				prefix.add("values", root.build(ops.empty()));
			return prefix;
		}
	}
}
