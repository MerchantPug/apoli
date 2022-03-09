package io.github.edwinmindcraft.apoli.common.action.meta;

import com.mojang.serialization.Codec;

public record NothingConfiguration<T>() implements IDelegatedActionConfiguration<T> {
	public static <T> Codec<NothingConfiguration<T> > codec() {
		return Codec.unit(NothingConfiguration::new);
	}
	@Override
	public void execute(T parameters) {}
}
