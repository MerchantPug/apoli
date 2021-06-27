package dev.experimental.apoli.common.registry.action;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemAction;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.factory.ItemAction;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.action.item.ConsumeAction;
import dev.experimental.apoli.common.action.item.DelegatedItemAction;
import io.github.apace100.apoli.Apoli;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ModItemActions {
	public static final BiConsumer<ConfiguredItemAction<?, ?>, ItemStack> EXECUTOR = (action, stack) -> action.execute(stack);
	public static final BiPredicate<ConfiguredItemCondition<?, ?>, ItemStack> PREDICATE = (condition, stack) -> condition.check(stack);

	public static final RegistrySupplier<ConsumeAction> CONSUME = register("consume", ConsumeAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(ApoliRegistries.ITEM_ACTION, DelegatedItemAction::new, ConfiguredItemAction.CODEC, ConfiguredItemCondition.CODEC, EXECUTOR, PREDICATE);
	}

	private static <T extends ItemAction<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.ITEM_ACTION.register(Apoli.identifier(name), factory);
	}
}
