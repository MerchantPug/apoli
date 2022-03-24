package io.github.edwinmindcraft.apoli.common.condition.biome;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;
import java.util.function.Function;

public class PropertyBiomeCondition<E extends Enum<E>> extends BiomeCondition<FieldConfiguration<E>> {

	private final Function<Holder<Biome>, E> function;

	public PropertyBiomeCondition(String field, Codec<E> codec, Function<Holder<Biome>, E> function) {
		super(FieldConfiguration.codec(codec, field));
		this.function = function;
	}

	@Override
	protected boolean check(FieldConfiguration<E> configuration, Holder<Biome> biome) {
		return Objects.equals(this.function.apply(biome), configuration.value());
	}
}
