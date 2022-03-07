package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import java.util.Optional;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class PreventItemActionPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredItemCondition<?, ?>>>> {

	public static boolean isUsagePrevented(Entity entity, ItemStack stack) {
		return IPowerContainer.getPowers(entity, ApoliPowers.PREVENT_ITEM_USAGE.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, stack));
	}

	public PreventItemActionPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredItemCondition.CODEC, "item_condition"));
	}

	public boolean doesPrevent(ConfiguredPower<FieldConfiguration<Optional<ConfiguredItemCondition<?, ?>>>, ?> configuration, ItemStack stack) {
		//FIXME Disable Food Restrictions.
		if (stack.isEdible() && !ApoliAPI.enableFoodRestrictions())
			return false;
		return configuration.getConfiguration().value().map(x -> x.check(stack)).orElse(true);
	}
}
