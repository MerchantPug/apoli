package io.github.edwinmindcraft.apoli.common.registry;

import io.github.apace100.apoli.util.AddPowerLootFunction;
import io.github.apace100.apoli.util.RemovePowerLootFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.RegistryObject;

public class ApoliLootFunctions {
	public static final RegistryObject<LootItemFunctionType> ADD_POWER_LOOT_FUNCTION = ApoliRegisters.LOOT_FUNCTIONS.register("add_power", () -> new LootItemFunctionType(new AddPowerLootFunction.Serializer()));
	public static final RegistryObject<LootItemFunctionType> REMOVE_POWER_LOOT_FUNCTION = ApoliRegisters.LOOT_FUNCTIONS.register("remove_power", () -> new LootItemFunctionType(new RemovePowerLootFunction.Serializer()));


	/**
	 * Forge is weird, so this need to be initialized during {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent FMLCommonSetupEvent}
	 * to be forward compatible with 1.18.2.
	 */
	public static void bootstrap() {}
}
