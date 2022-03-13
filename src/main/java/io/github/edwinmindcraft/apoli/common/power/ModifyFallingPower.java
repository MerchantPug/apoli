package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

import java.lang.reflect.Field;
import java.util.OptionalDouble;
import java.util.UUID;

public class ModifyFallingPower extends PowerFactory<ModifyFallingConfiguration> {

	private static final UUID SLOW_FALLING_ID;
	private static final AttributeModifier SLOW_FALLING;

	static {
		try {
			Field sfi = LivingEntity.class.getDeclaredField("SLOW_FALLING_ID");
			sfi.setAccessible(true);
			SLOW_FALLING_ID = (UUID) sfi.get(null);
			Field sf = LivingEntity.class.getDeclaredField("SLOW_FALLING");
			sf.setAccessible(true);
			SLOW_FALLING = (AttributeModifier) sf.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void apply(Entity entity, boolean isFalling) {
		if (!(entity instanceof LivingEntity living))
			return;
		AttributeInstance attribute = living.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
		if (attribute == null) return;
		//Compare references because equals is busted.
		OptionalDouble max = IPowerContainer.getPowers(living, ApoliPowers.MODIFY_FALLING.get()).stream().map(ConfiguredPower::getConfiguration).mapToDouble(ModifyFallingConfiguration::velocity).min();
		if (max.isEmpty()) return;
		double modifier = max.getAsDouble() - 0.08D;
		AttributeModifier mod = attribute.getModifier(SLOW_FALLING_ID);
		if (mod == SLOW_FALLING && modifier >= -0.07D) return;
		if (isFalling) {
			if (mod != null) {
				if (mod.getAmount() == modifier) return; //The same modifier is already applied.
				attribute.removeModifier(mod);
			}
			attribute.addTransientModifier(new AttributeModifier(SLOW_FALLING_ID, "Apoli powers", modifier, AttributeModifier.Operation.ADDITION));
		} else {
			attribute.removeModifier(SLOW_FALLING_ID);
		}
	}

	public ModifyFallingPower() {
		super(ModifyFallingConfiguration.CODEC, true);
	}

	private void add(ConfiguredPower<ModifyFallingConfiguration, ?> configuration, Entity entity) {
		if (!(entity instanceof LivingEntity living))
			return;
		AttributeInstance attribute = living.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
		AttributeModifier modifier = configuration.getConfiguration().modifier(configuration.getRegistryName());
		if (attribute != null && !attribute.hasModifier(modifier))
			attribute.addTransientModifier(modifier);
	}

	private void remove(ConfiguredPower<ModifyFallingConfiguration, ?> configuration, Entity entity) {
		if (!(entity instanceof LivingEntity living))
			return;
		AttributeInstance attribute = living.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
		AttributeModifier modifier = configuration.getConfiguration().modifier(configuration.getRegistryName());
		if (attribute != null && attribute.hasModifier(modifier))
			attribute.removeModifier(modifier);
	}
}
