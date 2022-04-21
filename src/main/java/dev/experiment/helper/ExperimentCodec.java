package dev.experiment.helper;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.BooleanSupplier;

public record ExperimentCodec<A>(Codec<A> stable, Codec<A> experimental,
								 BooleanSupplier allowExperimental) implements Codec<A> {

	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
		if (this.allowExperimental.getAsBoolean()) {
			DataResult<Pair<A, T>> result = this.experimental.decode(ops, input);
			if (result.result().isPresent())
				return result;
			String error = result.error().get().message();
			result = this.stable.decode(ops, input);
			if (result.result().isPresent())
				return result;
			return result.mapError(s -> "Failed to decode with errors: STABLE: %s, EXPERIMENTAL: %s".formatted(s, error));
		}
		return this.stable.decode(ops, input);
	}

	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
		if (this.allowExperimental.getAsBoolean()) {
			DataResult<T> result = this.experimental.encode(input, ops, prefix);
			if (result.result().isPresent())
				return result;
			String error = result.error().get().message();
			result = this.stable.encode(input, ops, prefix);
			if (result.result().isPresent())
				return result;
			return result.mapError(s -> "Failed to encode with errors: STABLE: %s, EXPERIMENTAL: %s".formatted(s, error));
		}
		return this.stable.encode(input, ops, prefix);
	}
}
