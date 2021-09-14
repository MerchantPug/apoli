package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_CONDITIONS;

public class ModItemConditions {

	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (config, stack) -> config.check(stack);

	public static final RegistryObject<SimpleItemCondition> FOOD = ITEM_CONDITIONS.register("food", () -> new SimpleItemCondition(ItemStack::isEdible));
	public static final RegistryObject<SimpleItemCondition> MEAT = ITEM_CONDITIONS.register("meat", () -> new SimpleItemCondition(stack -> stack.isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat()));
	public static final RegistryObject<IngredientCondition> INGREDIENT = ITEM_CONDITIONS.register("ingredient", IngredientCondition::new);
	public static final RegistryObject<ComparingItemCondition> ARMOR_VALUE = ITEM_CONDITIONS.register("armor_value", () -> new ComparingItemCondition(value -> value.getItem() instanceof ArmorItem ai ? ai.getDefense() : 0));
	public static final RegistryObject<ComparingItemCondition> HARVEST_LEVEL = ITEM_CONDITIONS.register("harvest_level", () -> new ComparingItemCondition(value -> value.getItem() instanceof DiggerItem ti ? ti.getTier().getLevel() : 0));
	public static final RegistryObject<EnchantmentCondition> ENCHANTMENT = ITEM_CONDITIONS.register("enchantment", EnchantmentCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ITEM_CONDITIONS, DelegatedItemCondition::new, ConfiguredItemCondition.CODEC, PREDICATE);
	}
}
