package io.github.apace100.apoli.power.factory.action;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

public class ItemActions {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ActionFactory<>(Apoli.identifier("consume"), new SerializableData()
            .add("amount", SerializableDataTypes.INT, 1),
            (data, stack) -> {
                stack.decrement(data.getInt("amount"));
            }));
    }

    private static void register(ActionFactory<ItemStack> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
