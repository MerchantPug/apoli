package io.github.apace100.apoli.access;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTables;

public interface IdentifiedLootTable {

    void setId(ResourceLocation id, LootTables lootManager);

    ResourceLocation getId();
}