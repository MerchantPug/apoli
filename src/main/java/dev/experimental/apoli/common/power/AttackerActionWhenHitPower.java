package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.CooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.ActionWhenHitConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class AttackerActionWhenHitPower extends CooldownPowerFactory.Simple<ActionWhenHitConfiguration> {
	public static void execute(PlayerEntity player, DamageSource damageSource, float amount) {
		IPowerContainer.getPowers(player, ModPowers.ATTACKER_ACTION_WHEN_HIT.get()).forEach(x -> x.getFactory().whenHit(x, player, damageSource, amount));
	}

	public AttackerActionWhenHitPower() {
		super(ActionWhenHitConfiguration.CODEC);
	}

	public void whenHit(ConfiguredPower<ActionWhenHitConfiguration, ?> configuration, PlayerEntity player, DamageSource damageSource, float damageAmount) {
		if (damageSource.getAttacker() != null && damageSource.getAttacker() != player) {
			if (ConfiguredDamageCondition.check(configuration.getConfiguration().damageCondition(), damageSource, damageAmount)) {
				if (this.canUse(configuration, player)) {
					configuration.getConfiguration().entityAction().execute(damageSource.getAttacker());
					this.use(configuration, player);
				}
			}
		}
	}
}
