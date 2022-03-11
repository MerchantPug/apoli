package io.github.edwinmindcraft.apoli.common.registry;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.util.AddPowerLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ApoliLootFunctions {
	public static final LootItemFunctionType ADD_POWER_LOOT_FUNCTION = register("add_power", new LootItemFunctionType(new AddPowerLootFunction.Serializer()));

	/**
	 * Forge is weird, so this need to be initialized during {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent FMLCommonSetupEvent}
	 * to be forward compatible with 1.18.2.
	 */
	public static void bootstrap() {}

	private static LootItemFunctionType register(String name, LootItemFunctionType type) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, Apoli.identifier(name), type);
	}
}
