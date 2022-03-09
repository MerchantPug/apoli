package io.github.edwinmindcraft.apoli.api;

import org.apache.commons.lang3.mutable.Mutable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record VariableAccess<T>(Supplier<T> reader, Consumer<T> writer) implements Mutable<T>{
	@Override
	public T getValue() {
		return this.reader().get();
	}

	@Override
	public void setValue(T value) {
		this.writer().accept(value);
	}
}
