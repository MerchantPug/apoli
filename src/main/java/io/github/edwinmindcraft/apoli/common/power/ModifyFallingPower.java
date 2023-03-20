package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;

public class ModifyFallingPower extends ValueModifyingPowerFactory<ModifyFallingConfiguration> {

	public ModifyFallingPower() {
		super(ModifyFallingConfiguration.CODEC);
	}

	// As Origins Fabric now utilises special modifiers for this class, we are now unable to hook into Forge's Entity Gravity attribute.

	/*
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

	@Deprecated
	public static void apply(Entity entity, boolean isFalling) {
		if (!(entity instanceof LivingEntity living))
			return;
		AttributeInstance attribute = living.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
		if (attribute == null) return;
		//Compare references because equals is busted.
		OptionalDouble max = IPowerContainer.getPowers(living, ApoliPowers.MODIFY_FALLING.get()).stream().map(Holder::value).map(ConfiguredPower::getConfiguration).mapToDouble(ModifyFallingConfiguration::velocity).min();
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
	 */
}
