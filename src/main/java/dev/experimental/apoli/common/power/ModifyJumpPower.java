package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.experimental.apoli.common.power.configuration.ModifyJumpConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.LivingEntity;

public class ModifyJumpPower extends ValueModifyingPowerFactory<ModifyJumpConfiguration> {
	public static double apply(LivingEntity player, double baseValue) {
		return IPowerContainer.modify(player, ModPowers.MODIFY_JUMP.get(), baseValue, x -> true, x -> x.getFactory().execute(x, player));
	}

	public ModifyJumpPower() {
		super(ModifyJumpConfiguration.CODEC);
	}

	public void execute(ConfiguredPower<ModifyJumpConfiguration, ?> configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.getConfiguration().condition(), player);
	}
}
