package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ModifyFoodConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import io.github.apace100.apoli.util.AttributeUtil;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModifyFoodPower extends PowerFactory<ModifyFoodConfiguration> {

	public static double apply(Player source, ItemStack stack, double baseValue, Function<ModifyFoodConfiguration, ListConfiguration<AttributeModifier>> access) {
		List<AttributeModifier> modifiers = IPowerContainer.getPowers(source, ModPowers.MODIFY_FOOD.get()).stream()
				.filter(x -> x.getFactory().check(x, stack))
				.flatMap(x -> access.apply(x.getConfiguration()).getContent().stream()).collect(Collectors.toList());
		return AttributeUtil.applyModifiers(modifiers, baseValue);
	}

	public static void execute(Player source, ItemStack stack) {
		IPowerContainer.getPowers(source, ModPowers.MODIFY_FOOD.get()).stream()
				.filter(x -> x.getFactory().check(x, stack))
				.forEach(x -> x.getFactory().execute(x, source));
	}

	public ModifyFoodPower() {
		super(ModifyFoodConfiguration.CODEC);
	}

	public boolean check(ConfiguredPower<ModifyFoodConfiguration, ?> config, ItemStack stack) {
		return ConfiguredItemCondition.check(config.getConfiguration().itemCondition(), stack);
	}

	public void execute(ConfiguredPower<ModifyFoodConfiguration, ?> config, Player player) {
		ConfiguredEntityAction.execute(config.getConfiguration().entityAction(), player);
	}
}
