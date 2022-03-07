package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.LivingEntity;

public class ActionOnLandPower extends PowerFactory<FieldConfiguration<ConfiguredEntityAction<?, ?>>> {
	public static void execute(LivingEntity player) {
		var ls = IPowerContainer.getPowers(player, ApoliPowers.ACTION_ON_LAND.get());
		ls.forEach(x -> x.getFactory().executeAction(x, player));
	}

	public ActionOnLandPower() {
		super(FieldConfiguration.codec(ConfiguredEntityAction.CODEC, "action_on_land"));
	}

	public void executeAction(ConfiguredPower<FieldConfiguration<ConfiguredEntityAction<?, ?>>, ?> config, LivingEntity player) {
		config.getConfiguration().value().execute(player);
	}
}
