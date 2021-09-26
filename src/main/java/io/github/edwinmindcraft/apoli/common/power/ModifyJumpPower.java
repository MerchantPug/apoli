package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyJumpConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.LivingEntity;

public class ModifyJumpPower extends ValueModifyingPowerFactory<ModifyJumpConfiguration> {
	public static double apply(LivingEntity player, double baseValue) {
		return IPowerContainer.modify(player, ApoliPowers.MODIFY_JUMP.get(), baseValue, x -> true, x -> x.getFactory().execute(x, player));
	}

	public ModifyJumpPower() {
		super(ModifyJumpConfiguration.CODEC);
	}

	public void execute(ConfiguredPower<ModifyJumpConfiguration, ?> configuration, LivingEntity player) {
		ConfiguredEntityAction.execute(configuration.getConfiguration().condition(), player);
	}
}
