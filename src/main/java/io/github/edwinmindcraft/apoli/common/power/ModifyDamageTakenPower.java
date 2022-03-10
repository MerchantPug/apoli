package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyDamageTakenConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class ModifyDamageTakenPower extends ValueModifyingPowerFactory<ModifyDamageTakenConfiguration> {
	public static float modify(Entity entity, DamageSource source, float amount) {
		return IPowerContainer.modify(entity, ApoliPowers.MODIFY_DAMAGE_TAKEN.get(), amount, x -> x.getFactory().check(x, entity, source, amount), x -> x.getFactory().execute(x, entity, source));
	}

	public ModifyDamageTakenPower() {
		super(ModifyDamageTakenConfiguration.CODEC);
	}

	public boolean check(ConfiguredPower<ModifyDamageTakenConfiguration, ?> config, Entity entity, DamageSource source, float amount) {
		ModifyDamageTakenConfiguration configuration = config.getConfiguration();
		boolean damage = ConfiguredDamageCondition.check(configuration.damageCondition(), source, amount);
		if (!damage) return false;
		Entity attacker = source.getEntity();
		return attacker == null ?
				config.getConfiguration().biEntityCondition() == null :
				ConfiguredBiEntityCondition.check(configuration.biEntityCondition(), source.getEntity(), entity);
	}

	public void execute(ConfiguredPower<ModifyDamageTakenConfiguration, ?> config, Entity entity, DamageSource source) {
		ModifyDamageTakenConfiguration configuration = config.getConfiguration();
		ConfiguredEntityAction.execute(configuration.selfAction(), entity);
		if (source.getEntity() != null) {
			ConfiguredEntityAction.execute(configuration.targetAction(), source.getEntity());
			ConfiguredBiEntityAction.execute(configuration.biEntityAction(), source.getEntity(), entity);
		}
	}
}
