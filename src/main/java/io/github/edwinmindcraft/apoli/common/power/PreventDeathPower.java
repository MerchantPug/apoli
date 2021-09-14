package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.PreventDeathConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import java.util.Optional;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

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
