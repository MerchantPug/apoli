package io.github.edwinmindcraft.apoli.common.condition.biome;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BiomeCondition;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class InTagCondition extends BiomeCondition<FieldConfiguration<TagKey<Biome>>> {

    public InTagCondition() {
        super(FieldConfiguration.codec(SerializableDataTypes.BIOME_TAG, "tag"));
    }

    @Override
    public boolean check(FieldConfiguration<TagKey<Biome>> configuration, Holder<Biome> biome) {
        return biome.is(configuration.value());
    }
}
