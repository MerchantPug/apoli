package io.github.edwinmindcraft.apoli.common.registry.condition;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.item.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_CONDITIONS;

public class ApoliItemConditions {

	public static final BiPredicate<ConfiguredItemCondition<?, ?>, Pair<Level, ItemStack>> PREDICATE = (config, pair) -> config.check(pair.getFirst(), pair.getSecond());

	private static <U extends ItemCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.ITEM_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedItemCondition<ConstantConfiguration<Pair<Level, ItemStack>>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedItemCondition<ConditionStreamConfiguration<ConfiguredItemCondition<?, ?>, Pair<Level, ItemStack>>>> AND = of("and");
	public static final RegistryObject<DelegatedItemCondition<ConditionStreamConfiguration<ConfiguredItemCondition<?, ?>, Pair<Level, ItemStack>>>> OR = of("or");

	public static final RegistryObject<SimpleItemCondition> FOOD = ITEM_CONDITIONS.register("food", () -> new SimpleItemCondition(ItemStack::isEdible));
	public static final RegistryObject<SimpleItemCondition> MEAT = ITEM_CONDITIONS.register("meat", () -> new SimpleItemCondition(stack -> stack.isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat()));
	public static final RegistryObject<IngredientCondition> INGREDIENT = ITEM_CONDITIONS.register("ingredient", IngredientCondition::new);
	public static final RegistryObject<ComparingItemCondition> ARMOR_VALUE = ITEM_CONDITIONS.register("armor_value", () -> new ComparingItemCondition(value -> value.getItem() instanceof ArmorItem ai ? ai.getDefense() : 0));
	public static final RegistryObject<ComparingItemCondition> HARVEST_LEVEL = ITEM_CONDITIONS.register("harvest_level", () -> new ComparingItemCondition(value -> value.getItem() instanceof DiggerItem ti ? ti.getTier().getLevel() : 0));
	public static final RegistryObject<EnchantmentCondition> ENCHANTMENT = ITEM_CONDITIONS.register("enchantment", EnchantmentCondition::new);
	public static final RegistryObject<NbtCondition> NBT = ITEM_CONDITIONS.register("nbt", NbtCondition::new);
	public static final RegistryObject<SimpleItemCondition> FIREPROOF = ITEM_CONDITIONS.register("fireproof", () -> new SimpleItemCondition(x -> x.getItem().isFireResistant()));
	public static final RegistryObject<SimpleItemCondition> ENCHANTABLE = ITEM_CONDITIONS.register("enchantable", () -> new SimpleItemCondition(ItemStack::isEnchantable));

	public static ConfiguredItemCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	public static ConfiguredItemCondition<?, ?> and(ConfiguredItemCondition<?, ?>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredItemCondition<?, ?> or(ConfiguredItemCondition<?, ?>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(ITEM_CONDITIONS, DelegatedItemCondition::new, ConfiguredItemCondition.CODEC, PREDICATE);
	}
}
