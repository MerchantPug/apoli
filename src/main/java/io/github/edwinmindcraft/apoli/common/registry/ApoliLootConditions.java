package io.github.edwinmindcraft.apoli.common.registry;

import io.github.apace100.apoli.util.PowerLootCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.RegistryObject;

public class ApoliLootConditions {
	public static final RegistryObject<LootItemConditionType> POWER_LOOT_CONDITION = ApoliRegisters.LOOT_CONDITIONS.register("power", () -> new LootItemConditionType(new PowerLootCondition.Serializer()));

	/**
	 * Forge is weird, so this need to be initialized during {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent FMLCommonSetupEvent}
	 * to be forward compatible with 1.18.2.
	 */
	public static void bootstrap() {}
}
