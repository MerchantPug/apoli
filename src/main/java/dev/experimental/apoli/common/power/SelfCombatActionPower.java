package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.CooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.ConditionedCombatActionConfiguration;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static dev.experimental.apoli.common.registry.ModPowers.SELF_ACTION_ON_HIT;
import static dev.experimental.apoli.common.registry.ModPowers.SELF_ACTION_ON_KILL;

public class SelfCombatActionPower extends CooldownPowerFactory.Simple<ConditionedCombatActionConfiguration> {

	public static void onHit(LivingEntity player, LivingEntity target, DamageSource source, float amount) {
		IPowerContainer.getPowers(player, SELF_ACTION_ON_HIT.get()).forEach(x -> x.getFactory().execute(x, player, target, source, amount));
	}

	public static void onKill(LivingEntity player, LivingEntity target, DamageSource source, float amount) {
		IPowerContainer.getPowers(player, SELF_ACTION_ON_KILL.get()).forEach(x -> x.getFactory().execute(x, player, target, source, amount));
	}

	public SelfCombatActionPower() {
		super(ConditionedCombatActionConfiguration.CODEC);
	}

	public void execute(ConfiguredPower<ConditionedCombatActionConfiguration, ?> configuration, LivingEntity player, LivingEntity target, DamageSource source, float amount) {
		if (configuration.getConfiguration().check(target, source, amount) && canUse(configuration, player)) {
			configuration.getConfiguration().entityAction().execute(player);
			use(configuration, player);
		}
	}
}
