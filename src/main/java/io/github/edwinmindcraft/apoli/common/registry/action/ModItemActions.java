package io.github.edwinmindcraft.apoli.common.registry.action;

import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.common.action.item.ConsumeAction;
import io.github.edwinmindcraft.apoli.common.action.item.DelegatedItemAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_ACTIONS;

public class ModItemActions {
	public static final BiConsumer<ConfiguredItemAction<?, ?>, ItemStack> EXECUTOR = (action, stack) -> action.execute(stack);
	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (condition, stack) -> condition.check(stack);

	public static final RegistryObject<ConsumeAction> CONSUME = ITEM_ACTIONS.register("consume", ConsumeAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(ITEM_ACTIONS, DelegatedItemAction::new, ConfiguredItemAction.CODEC, ConfiguredItemCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
