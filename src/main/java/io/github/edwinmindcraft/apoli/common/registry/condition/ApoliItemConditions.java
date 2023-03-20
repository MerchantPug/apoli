package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.item.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_CONDITIONS;

public class ApoliItemConditions {

	public static final BiPredicate<ConfiguredItemCondition<?, ?>, Pair<Level, ItemStack>> PREDICATE = (config, pair) -> config.check(pair.getKey(), pair.getValue());

	private static <U extends ItemCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.create(Apoli.identifier(name), ApoliRegistries.ITEM_CONDITION_KEY.location(), Apoli.MODID);
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
	public static final RegistryObject<SimpleItemCondition> EMPTY = ITEM_CONDITIONS.register("empty", () -> new SimpleItemCondition(ItemStack::isEmpty));
	public static final RegistryObject<IntComparingItemCondition> AMOUNT = ITEM_CONDITIONS.register("amount", () -> new IntComparingItemCondition(ItemStack::getCount));
	public static final RegistryObject<PowerCountCondition> POWER_COUNT = ITEM_CONDITIONS.register("power_count", PowerCountCondition::new);
	public static final RegistryObject<ItemHasPowerCondition> HAS_POWER = ITEM_CONDITIONS.register("has_power", ItemHasPowerCondition::new);
	public static final RegistryObject<SimpleItemCondition> SMELTABLE = ITEM_CONDITIONS.register("smeltable", () -> new SimpleItemCondition((level, stack) -> SimpleItemCondition.forCookingRecipeType(level, stack, RecipeType.SMELTING)));
	public static final RegistryObject<SimpleItemCondition> IS_DAMAGEABLE = ITEM_CONDITIONS.register("is_damageable", () -> new SimpleItemCondition(ItemStack::isDamageableItem));
	public static final RegistryObject<IntComparingItemCondition> DURABILITY = ITEM_CONDITIONS.register("durability", () -> new IntComparingItemCondition(stack -> stack.getMaxDamage() - stack.getDamageValue()));
	public static final RegistryObject<FloatComparingItemCondition> RELATIVE_DURABILITY = ITEM_CONDITIONS.register("relative_durability", () -> new FloatComparingItemCondition(stack -> (float)(stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage()));
	public static final RegistryObject<IsEquippableCondition> IS_EQUIPPABLE = ITEM_CONDITIONS.register("is_equippable", IsEquippableCondition::new);

	public static ConfiguredItemCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	@SafeVarargs
	public static ConfiguredItemCondition<?, ?> and(HolderSet<ConfiguredItemCondition<?, ?>>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredItemCondition<?, ?> and(ConfiguredItemCondition<?, ?>... conditions) {return and(HolderSet.direct(Holder::direct, conditions));}

	@SafeVarargs
	public static ConfiguredItemCondition<?, ?> or(HolderSet<ConfiguredItemCondition<?, ?>>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(ITEM_CONDITIONS, DelegatedItemCondition::new, ConfiguredItemCondition.CODEC_SET, PREDICATE);
	}
}
