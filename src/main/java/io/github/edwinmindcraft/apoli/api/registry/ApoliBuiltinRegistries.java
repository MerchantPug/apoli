package io.github.edwinmindcraft.apoli.api.registry;

import io.github.apace100.calio.ClassUtil;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ApoliBuiltinRegistries {
	public static final Class<ConfiguredPower<?, ?>> CONFIGURED_POWER_CLASS = ClassUtil.get();

	public static final Class<ConfiguredBiEntityAction<?, ?>> CONFIGURED_BIENTITY_ACTION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredBlockAction<?, ?>> CONFIGURED_BLOCK_ACTION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredEntityAction<?, ?>> CONFIGURED_ENTITY_ACTION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredItemAction<?, ?>> CONFIGURED_ITEM_ACTION_CLASS = ClassUtil.get();

	public static final Class<ConfiguredBiEntityCondition<?, ?>> CONFIGURED_BIENTITY_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredBiomeCondition<?, ?>> CONFIGURED_BIOME_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredBlockCondition<?, ?>> CONFIGURED_BLOCK_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredDamageCondition<?, ?>> CONFIGURED_DAMAGE_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredEntityCondition<?, ?>> CONFIGURED_ENTITY_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredFluidCondition<?, ?>> CONFIGURED_FLUID_CONDITION_CLASS = ClassUtil.get();
	public static final Class<ConfiguredItemCondition<?, ?>> CONFIGURED_ITEM_CONDITION_CLASS = ClassUtil.get();

	public static Supplier<IForgeRegistry<ConfiguredPower<?, ?>>> CONFIGURED_POWERS;
	
	public static Supplier<IForgeRegistry<ConfiguredBiEntityAction<?, ?>>> CONFIGURED_BIENTITY_ACTIONS;
	public static Supplier<IForgeRegistry<ConfiguredBlockAction<?, ?>>> CONFIGURED_BLOCK_ACTIONS;
	public static Supplier<IForgeRegistry<ConfiguredEntityAction<?, ?>>> CONFIGURED_ENTITY_ACTIONS;
	public static Supplier<IForgeRegistry<ConfiguredItemAction<?, ?>>> CONFIGURED_ITEM_ACTIONS;

	public static Supplier<IForgeRegistry<ConfiguredBiEntityCondition<?, ?>>> CONFIGURED_BIENTITY_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredBiomeCondition<?, ?>>> CONFIGURED_BIOME_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredBlockCondition<?, ?>>> CONFIGURED_BLOCK_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredDamageCondition<?, ?>>> CONFIGURED_DAMAGE_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredEntityCondition<?, ?>>> CONFIGURED_ENTITY_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredFluidCondition<?, ?>>> CONFIGURED_FLUID_CONDITIONS;
	public static Supplier<IForgeRegistry<ConfiguredItemCondition<?, ?>>> CONFIGURED_ITEM_CONDITIONS;
}
