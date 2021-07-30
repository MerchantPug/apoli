package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.LivingEntity;

public class ActionOnLandPower extends PowerFactory<FieldConfiguration<ConfiguredEntityAction<?, ?>>> {
	public static void execute(LivingEntity player) {
		IPowerContainer.getPowers(player, ModPowers.ACTION_ON_LAND.get()).forEach(x -> x.getFactory().executeAction(x, player));
	}

	public ActionOnLandPower() {
		super(FieldConfiguration.codec(ConfiguredEntityAction.CODEC, "action_on_land"));
	}

	public void executeAction(ConfiguredPower<FieldConfiguration<ConfiguredEntityAction<?, ?>>, ?> config, LivingEntity player) {
		config.getConfiguration().value().execute(player);
	}
}
