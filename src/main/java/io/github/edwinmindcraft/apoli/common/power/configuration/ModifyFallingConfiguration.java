package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliModifierOperations;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import java.util.Objects;
import java.util.Optional;

public final class ModifyFallingConfiguration implements IValueModifyingPowerConfiguration {
	public static final Codec<ModifyFallingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(CalioCodecHelper.DOUBLE, "velocity").forGetter(ModifyFallingConfiguration::velocity),
			CalioCodecHelper.optionalField(CalioCodecHelper.BOOL, "take_fall_damage", true).forGetter(ModifyFallingConfiguration::takeFallDamage),
			ListConfiguration.MODIFIER_CODEC.forGetter(ModifyFallingConfiguration::modifiers)
	).apply(instance, ModifyFallingConfiguration::new));

	private final Optional<Double> velocity;
	private final boolean takeFallDamage;
	private final ListConfiguration<ConfiguredModifier<?>> modifiers;

	public ModifyFallingConfiguration(Optional<Double> velocity, boolean takeFallDamage, ListConfiguration<ConfiguredModifier<?>> modifiers) {
		this.velocity = velocity;
		this.takeFallDamage = takeFallDamage;
		if (velocity.isPresent() && modifiers.getContent().isEmpty())
			this.modifiers = ListConfiguration.of(modifier(velocity.get()));
		else
			this.modifiers = modifiers;
	}

	public ConfiguredModifier<?> modifier(double velocity) {
		return ModifierUtil.createSimpleModifier(ApoliModifierOperations.SET_TOTAL::get, velocity);
	}

	public Optional<Double> velocity() {
		return this.velocity;
	}

	public boolean takeFallDamage() {
		return this.takeFallDamage;
	}

	@Override
	public ListConfiguration<ConfiguredModifier<?>> modifiers() {
		return this.modifiers;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ModifyFallingConfiguration) obj;
		return this.velocity().isPresent() == that.velocity().isPresent() &&
				(this.velocity().isEmpty() || Double.doubleToLongBits(this.velocity.get()) == Double.doubleToLongBits(that.velocity.get()))
				&& this.takeFallDamage == that.takeFallDamage
				&& this.modifiers.entries() == that.modifiers.entries();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.velocity, this.takeFallDamage, this.modifiers);
	}

	@Override
	public String toString() {
		return "ModifyFallingConfiguration[" +
				"velocity=" + this.velocity + ", " +
				"takeFallDamage=" + this.takeFallDamage + ", " +
				"modifiers=" + this.modifiers + ']';
	}
}
