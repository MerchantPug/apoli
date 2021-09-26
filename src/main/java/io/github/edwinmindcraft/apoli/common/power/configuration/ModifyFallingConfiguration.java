package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public final class ModifyFallingConfiguration implements IValueModifyingPowerConfiguration {
	private static final Random RANDOM = new Random();

	public static final Codec<ModifyFallingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("velocity").forGetter(ModifyFallingConfiguration::velocity),
			Codec.BOOL.optionalFieldOf("take_fall_damage", true).forGetter(ModifyFallingConfiguration::takeFallDamage)
	).apply(instance, ModifyFallingConfiguration::new));
	private final double velocity;
	private final boolean takeFallDamage;

	public ModifyFallingConfiguration(double velocity, boolean takeFallDamage) {
		this.velocity = velocity;
		this.takeFallDamage = takeFallDamage;
	}

	private final Lazy<ListConfiguration<AttributeModifier>> cache = Lazy.of(() -> {
		String name = "MFF" + this.velocity() + "**" + this.takeFallDamage();
		RANDOM.setSeed(name.hashCode() & ((name.hashCode() ^ 0x52D529L) << 16L));
		UUID uuid = new UUID(RANDOM.nextLong(), RANDOM.nextLong());
		return new ListConfiguration<>(ImmutableList.of(new AttributeModifier(uuid, "Unnamed gravity", this.velocity() - 0.08F, AttributeModifier.Operation.ADDITION)));
	});

	@Override
	public ListConfiguration<AttributeModifier> modifiers() {
		return this.cache.get();
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
