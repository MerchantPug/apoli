package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public final class ModifyFallingConfiguration implements IDynamicFeatureConfiguration {
	private static final Random RANDOM = new Random();

	public static final Codec<ModifyFallingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("velocity").forGetter(ModifyFallingConfiguration::velocity),
			CalioCodecHelper.optionalField(Codec.BOOL, "take_fall_damage", true).forGetter(ModifyFallingConfiguration::takeFallDamage)
	).apply(instance, ModifyFallingConfiguration::new));

	private final double velocity;
	private final boolean takeFallDamage;

	public ModifyFallingConfiguration(double velocity, boolean takeFallDamage) {
		this.velocity = velocity;
		this.takeFallDamage = takeFallDamage;
	}

	private AttributeModifier cache;

	public AttributeModifier modifier(ResourceLocation registryName) {
		if (this.cache == null) {
			RANDOM.setSeed(((long) registryName.getNamespace().hashCode() << 32L) | (long) registryName.getPath().hashCode());
			UUID uuid = new UUID(RANDOM.nextLong(), RANDOM.nextLong());
			this.cache = new AttributeModifier(uuid, "Modify Falling", this.velocity() - 0.08F, AttributeModifier.Operation.ADDITION);
		}
		return this.cache;
	}

	public double velocity() {return this.velocity;}

	public boolean takeFallDamage() {return this.takeFallDamage;}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ModifyFallingConfiguration) obj;
		return Double.doubleToLongBits(this.velocity) == Double.doubleToLongBits(that.velocity) &&
			   this.takeFallDamage == that.takeFallDamage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.velocity, this.takeFallDamage);
	}

	@Override
	public String toString() {
		return "ModifyFallingConfiguration[" +
			   "velocity=" + this.velocity + ", " +
			   "takeFallDamage=" + this.takeFallDamage + ']';
	}
}
