package io.github.edwinmindcraft.apoli.common.registry.action;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.action.item.ConsumeAction;
import io.github.edwinmindcraft.apoli.common.action.item.DelegatedItemAction;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_ACTIONS;

public class ApoliItemActions {
	public static final BiConsumer<ConfiguredItemAction<?, ?>, ItemStack> EXECUTOR = (action, stack) -> action.execute(stack);
	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (condition, stack) -> condition.check(stack);

	private static <U extends ItemAction<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.ITEM_ACTION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedItemAction<StreamConfiguration<ConfiguredItemAction<?, ?>, ItemStack>>> AND = of("and");
	public static final RegistryObject<DelegatedItemAction<ChanceConfiguration<ConfiguredItemAction<?, ?>, ItemStack>>> CHANCE = of("chance");
	public static final RegistryObject<DelegatedItemAction<IfElseConfiguration<ConfiguredItemCondition<?, ?>, ConfiguredItemAction<?, ?>, ItemStack>>> IF_ELSE = of("if_else");
	public static final RegistryObject<DelegatedItemAction<StreamConfiguration<ConfiguredItemAction<?, ?>, ItemStack>>> IF_ELSE_LIST = of("if_else_list");
	public static final RegistryObject<DelegatedItemAction<ChoiceConfiguration<ConfiguredItemAction<?, ?>, ItemStack>>> CHOICE = of("choice");
	public static final RegistryObject<DelegatedItemAction<DelayAction<ConfiguredItemAction<?, ?>, ItemStack>>> DELAY = of("delay");

	public static final RegistryObject<ConsumeAction> CONSUME = ITEM_ACTIONS.register("consume", ConsumeAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(ITEM_ACTIONS, DelegatedItemAction::new, ConfiguredItemAction.CODEC, ConfiguredItemCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
