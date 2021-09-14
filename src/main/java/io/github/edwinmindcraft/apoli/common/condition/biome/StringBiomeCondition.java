package io.github.edwinmindcraft.apoli.common.condition.biome;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.world.level.biome.Biome;

public class StringBiomeCondition extends BiomeCondition<FieldConfiguration<String>> {

	private final Function<Biome, String> function;

	public StringBiomeCondition(String field, Function<Biome, String> function) {
		super(FieldConfiguration.codec(Codec.STRING, field));
		this.function = function;
	}

	@Override
	protected boolean check(FieldConfiguration<String> configuration, Biome biome) {
		return Objects.equals(this.function.apply(biome), configuration.value());
	}
}
