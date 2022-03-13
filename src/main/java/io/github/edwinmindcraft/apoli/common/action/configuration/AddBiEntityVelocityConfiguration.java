package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Space;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

public record AddBiEntityVelocityConfiguration(Vector3f direction, boolean client, boolean server,
											   boolean set) implements IDynamicFeatureConfiguration {
	public static final Codec<AddBiEntityVelocityConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.VEC3F.forGetter(AddBiEntityVelocityConfiguration::direction),
			CalioCodecHelper.optionalField(Codec.BOOL, "client", true).forGetter(AddBiEntityVelocityConfiguration::client),
			CalioCodecHelper.optionalField(Codec.BOOL, "server", true).forGetter(AddBiEntityVelocityConfiguration::server),
			CalioCodecHelper.optionalField(Codec.BOOL, "set", false).forGetter(AddBiEntityVelocityConfiguration::set)
	).apply(instance, AddBiEntityVelocityConfiguration::new));
}
