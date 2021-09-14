package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.CooldownPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionWhenHitConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class SelfActionWhenHitPower extends CooldownPowerFactory.Simple<ActionWhenHitConfiguration> {
	public static void execute(LivingEntity player, DamageSource damageSource, float amount) {
		IPowerContainer.getPowers(player, ModPowers.SELF_ACTION_WHEN_HIT.get()).forEach(x -> x.getFactory().whenHit(x, player, damageSource, amount));
	}

	public SelfActionWhenHitPower() {
		super(ActionWhenHitConfiguration.CODEC);
	}

	public void whenHit(ConfiguredPower<ActionWhenHitConfiguration, ?> configuration, LivingEntity player, DamageSource damageSource, float damageAmount) {
		if (ConfiguredDamageCondition.check(configuration.getConfiguration().damageCondition(), damageSource, damageAmount)) {
			if (this.canUse(configuration, player)) {
				configuration.getConfiguration().entityAction().execute(player);
				this.use(configuration, player);
			}
		}
	}
}
