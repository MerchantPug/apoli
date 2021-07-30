package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.CooldownPowerFactory;
import dev.experimental.apoli.common.power.configuration.ActionWhenHitConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class AttackerActionWhenHitPower extends CooldownPowerFactory.Simple<ActionWhenHitConfiguration> {
	public static void execute(Player player, DamageSource damageSource, float amount) {
		IPowerContainer.getPowers(player, ModPowers.ATTACKER_ACTION_WHEN_HIT.get()).forEach(x -> x.getFactory().whenHit(x, player, damageSource, amount));
	}

	public AttackerActionWhenHitPower() {
		super(ActionWhenHitConfiguration.CODEC);
	}

	public void whenHit(ConfiguredPower<ActionWhenHitConfiguration, ?> configuration, Player player, DamageSource damageSource, float damageAmount) {
		if (damageSource.getEntity() != null && damageSource.getEntity() != player) {
			if (ConfiguredDamageCondition.check(configuration.getConfiguration().damageCondition(), damageSource, damageAmount)) {
				if (this.canUse(configuration, player)) {
					configuration.getConfiguration().entityAction().execute(damageSource.getEntity());
					this.use(configuration, player);
				}
			}
		}
	}
}
