package dev.experimental.apoli.common.registry.condition;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.factory.ItemCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.item.*;
import io.github.apace100.apoli.Apoli;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public class ModItemConditions {

	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (config, stack) -> config.check(stack);

	public static final RegistrySupplier<SimpleItemCondition> FOOD = register("food", () -> new SimpleItemCondition(ItemStack::isEdible));
	public static final RegistrySupplier<SimpleItemCondition> MEAT = register("meat", () -> new SimpleItemCondition(stack -> stack.isEdible() && stack.getItem().getFoodProperties().isMeat()));
	public static final RegistrySupplier<IngredientCondition> INGREDIENT = register("ingredient", IngredientCondition::new);
	public static final RegistrySupplier<ComparingItemCondition> ARMOR_VALUE = register("armor_value", () -> new ComparingItemCondition(value -> value.getItem() instanceof ArmorItem ai ? ai.getProtection() : 0));
	public static final RegistrySupplier<ComparingItemCondition> HARVEST_LEVEL = register("harvest_level", () -> new ComparingItemCondition(value -> value.getItem() instanceof ToolItem ti ? ti.getMaterial().getMiningLevel() : 0));
	public static final RegistrySupplier<EnchantmentCondition> ENCHANTMENT = register("enchantment", EnchantmentCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.ITEM_CONDITION, DelegatedItemCondition::new, ConfiguredItemCondition.CODEC, PREDICATE);
	}

	private static <T extends ItemCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.ITEM_CONDITION.register(Apoli.identifier(name), factory);
	}
}
