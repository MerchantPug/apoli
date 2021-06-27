package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.PreventDeathConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.Optional;

public class PreventDeathPower extends PowerFactory<PreventDeathConfiguration> {

	public static boolean tryPreventDeath(LivingEntity entity, DamageSource source, float amount) {
		Optional<ConfiguredPower<PreventDeathConfiguration, PreventDeathPower>> first = IPowerContainer.getPowers(entity, ModPowers.PREVENT_DEATH.get()).stream()
				.filter(x -> ConfiguredDamageCondition.check(x.getConfiguration().condition(), source, amount)).findFirst();
		first.ifPresent(x -> {
			entity.setHealth(1.0F);
			ConfiguredEntityAction.execute(x.getConfiguration().action(), entity);
		});
		return first.isPresent();
	}

	public PreventDeathPower() {
		super(PreventDeathConfiguration.CODEC);
	}
}
