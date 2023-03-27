package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IAttributeModifyingPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.apoli.common.util.PowerUtils;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

import java.util.Objects;

public final class LavaVisionConfiguration implements IAttributeModifyingPowerConfiguration {
	public static final Codec<LavaVisionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.FLOAT.fieldOf("s").forGetter(LavaVisionConfiguration::s),
			CalioCodecHelper.FLOAT.fieldOf("v").forGetter(LavaVisionConfiguration::v)
	).apply(instance, LavaVisionConfiguration::new));
	private final float s;
	private final float v;

	private final Lazy<ListConfiguration<AttributeModifier>> modifiers;

	public LavaVisionConfiguration(float s, float v) {
		this.s = s;
		this.v = v;
		this.modifiers = Lazy.of(() -> ListConfiguration.of(PowerUtils.staticModifier("Lava vision power", this.v() - 1.0, AttributeModifier.Operation.ADDITION, this.s(), this.v())));
	}

	@Override
	public ListConfiguration<AttributeModifier> modifiers() {
		return this.modifiers.get();
	}

	public float s() {
		return this.s;
	}

	public float v() {
		return this.v;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (LavaVisionConfiguration) obj;
		return Float.floatToIntBits(this.s) == Float.floatToIntBits(that.s) &&
			   Float.floatToIntBits(this.v) == Float.floatToIntBits(that.v);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.s, this.v);
	}

	@Override
	public String toString() {
		return "LavaVisionConfiguration[" +
			   "s=" + this.s + ", " +
			   "v=" + this.v + ']';
	}
}
