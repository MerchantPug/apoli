package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.CooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.ConditionedCombatActionConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

import static dev.experimental.apoli.common.registry.ModPowers.TARGET_ACTION_ON_HIT;

public class TargetCombatActionPower extends CooldownPowerFactory.Simple<ConditionedCombatActionConfiguration> {

	public static void onHit(PlayerEntity player, LivingEntity target, DamageSource source, float amount) {
		IPowerContainer.getPowers(player, TARGET_ACTION_ON_HIT.get()).forEach(x -> x.getFactory().execute(x, player, target, source, amount));
	}

	public TargetCombatActionPower() {
		super(ConditionedCombatActionConfiguration.CODEC);
	}

	public void execute(ConfiguredPower<ConditionedCombatActionConfiguration, ?> configuration, PlayerEntity player, LivingEntity target, DamageSource source, float amount) {
		if (configuration.getConfiguration().check(target, source, amount) && canUse(configuration, player)) {
			configuration.getConfiguration().entityAction().execute(target);
			use(configuration, player);
		}
	}
}
