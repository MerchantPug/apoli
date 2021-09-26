package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnItemUseConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ActionOnItemUsePower extends PowerFactory<ActionOnItemUseConfiguration> {
	public static void execute(LivingEntity player, ItemStack stack, ItemStack target) {
		IPowerContainer component = ApoliAPI.getPowerContainer(player);
		if (component != null)
			component.getPowers(ApoliPowers.ACTION_ON_ITEM_USE.get()).stream()
					.filter(x -> x.getFactory().doesApply(x, stack))
					.forEach(x -> x.getFactory().executeActions(x, player, target));
	}

	public ActionOnItemUsePower() {
		super(ActionOnItemUseConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredPower<ActionOnItemUseConfiguration, ?> factory, ItemStack stack) {
		ActionOnItemUseConfiguration configuration = factory.getConfiguration();
		return configuration.itemCondition() == null || configuration.itemCondition().check(stack);
	}

	public void executeActions(ConfiguredPower<ActionOnItemUseConfiguration, ?> factory, LivingEntity player, ItemStack stack) {
		ActionOnItemUseConfiguration configuration = factory.getConfiguration();
		if (configuration.itemAction() != null)
			configuration.itemAction().execute(stack);
		if (configuration.entityAction() != null)
			configuration.entityAction().execute(player);
	}
}
