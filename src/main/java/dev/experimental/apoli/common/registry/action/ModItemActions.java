package dev.experimental.apoli.common.registry.action;

import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.common.action.item.ConsumeAction;
import dev.experimental.apoli.common.action.item.DelegatedItemAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static dev.experimental.apoli.common.registry.ApoliRegisters.ITEM_ACTIONS;

public class ModItemActions {
	public static final BiConsumer<ConfiguredItemAction<?, ?>, ItemStack> EXECUTOR = (action, stack) -> action.execute(stack);
	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (condition, stack) -> condition.check(stack);

	public static final RegistryObject<ConsumeAction> CONSUME = ITEM_ACTIONS.register("consume", ConsumeAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(ITEM_ACTIONS, DelegatedItemAction::new, ConfiguredItemAction.CODEC, ConfiguredItemCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
