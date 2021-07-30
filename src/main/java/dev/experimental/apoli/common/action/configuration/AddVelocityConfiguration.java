package dev.experimental.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Space;

//FIXME Use a Map codec instead of x, y, z.
public record AddVelocityConfiguration(float x, float y, float z, Space space,
									   boolean set) implements IDynamicFeatureConfiguration {
	public static final Codec<AddVelocityConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("x", 0F).forGetter(AddVelocityConfiguration::x),
			Codec.FLOAT.optionalFieldOf("y", 0F).forGetter(AddVelocityConfiguration::y),
			Codec.FLOAT.optionalFieldOf("z", 0F).forGetter(AddVelocityConfiguration::z),
			ApoliDataTypes.SPACE.optionalFieldOf("space", Space.WORLD).forGetter(AddVelocityConfiguration::space),
			Codec.BOOL.optionalFieldOf("set", false).forGetter(AddVelocityConfiguration::set)
	).apply(instance, AddVelocityConfiguration::new));

	public Vec3f getVector() {
		return new Vec3f(this.x, this.y, this.z);
	}
}
