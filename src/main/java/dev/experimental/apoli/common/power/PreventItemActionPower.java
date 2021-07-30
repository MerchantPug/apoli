package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class PreventItemActionPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredItemCondition<?, ?>>>> {

	public static boolean isUsagePrevented(Entity entity, ItemStack stack) {
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_ITEM_USAGE.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, stack));
	}

	public PreventItemActionPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredItemCondition.CODEC, "item_condition"));
	}

	public boolean doesPrevent(ConfiguredPower<FieldConfiguration<Optional<ConfiguredItemCondition<?, ?>>>, ?> configuration, ItemStack stack) {
		//FIXME Disable Food Restrictions.
		return (!stack.isEdible() || !ApoliAPI.hasFoodRestrictions()) && configuration.getConfiguration().value().map(x -> x.check(stack)).orElse(true);
	}
}
