package io.github.edwinmindcraft.apoli.common.action.configuration;

import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Space;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

//FIXME Use a Map codec instead of x, y, z.
public record AddVelocityConfiguration(float x, float y, float z, Space space,
									   boolean set) implements IDynamicFeatureConfiguration {
	public static final Codec<AddVelocityConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.FLOAT, "x", 0F).forGetter(AddVelocityConfiguration::x),
			CalioCodecHelper.optionalField(Codec.FLOAT, "y", 0F).forGetter(AddVelocityConfiguration::y),
			CalioCodecHelper.optionalField(Codec.FLOAT, "z", 0F).forGetter(AddVelocityConfiguration::z),
			CalioCodecHelper.optionalField(ApoliDataTypes.SPACE, "space", Space.WORLD).forGetter(AddVelocityConfiguration::space),
			CalioCodecHelper.optionalField(Codec.BOOL, "set", false).forGetter(AddVelocityConfiguration::set)
	).apply(instance, AddVelocityConfiguration::new));

	public Vector3f getVector() {
		return new Vector3f(this.x, this.y, this.z);
	}
}
