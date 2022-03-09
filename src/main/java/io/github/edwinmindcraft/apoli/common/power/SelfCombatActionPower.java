package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.CooldownPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ConditionedCombatActionConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class SelfCombatActionPower extends CooldownPowerFactory.Simple<ConditionedCombatActionConfiguration> {

	public static void onHit(Entity player, Entity target, DamageSource source, float amount) {
		IPowerContainer.getPowers(player, ApoliPowers.SELF_ACTION_ON_HIT.get()).forEach(x -> x.getFactory().execute(x, player, target, source, amount));
	}

	public static void onKill(Entity player, Entity target, DamageSource source, float amount) {
		IPowerContainer.getPowers(player, ApoliPowers.SELF_ACTION_ON_KILL.get()).forEach(x -> x.getFactory().execute(x, player, target, source, amount));
	}

	public SelfCombatActionPower() {
		super(ConditionedCombatActionConfiguration.CODEC);
	}

	public void execute(ConfiguredPower<ConditionedCombatActionConfiguration, ?> configuration, Entity player, Entity target, DamageSource source, float amount) {
		if (configuration.getConfiguration().check(target, source, amount) && this.canUse(configuration, player)) {
			configuration.getConfiguration().entityAction().execute(player);
			this.use(configuration, player);
		}
	}
}
