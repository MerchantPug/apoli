package io.github.edwinmindcraft.apoli.common.action.meta;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.IStreamConfiguration;
import io.github.apace100.calio.FilterableWeightedList;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public record ChoiceConfiguration<T, V>(
		FilterableWeightedList<T> list,
		BiConsumer<T, V> executor) implements IDelegatedActionConfiguration<V>, IStreamConfiguration<T> {

	public static <T, V> Codec<ChoiceConfiguration<T, V>> codec(Codec<T> codec, BiConsumer<T, V> executor) {
		return CalioCodecHelper.weightedListOf(codec).fieldOf("actions").xmap(x -> new ChoiceConfiguration<>(x, executor), ChoiceConfiguration::list).codec();
	}

	@Override
	public void execute(V parameters) {
		this.executor().accept(this.list.pickRandom(new Random()), parameters);
	}

	@Override
	public List<T> entries() {
		return this.list().stream().toList();
	}
}
