package dev.experimental.apoli.common.registry;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.component.IPowerDataCache;
import dev.experimental.apoli.api.power.factory.*;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.util.PowerRestrictedCraftingRecipe;
import io.github.apace100.calio.ClassUtil;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;

public class ApoliRegisters {
	public static final DeferredRegister<PowerFactory<?>> POWER_FACTORIES = DeferredRegister.create(ClassUtil.<PowerFactory<?>>castClass(PowerFactory.class), Apoli.MODID);
	public static final DeferredRegister<EntityCondition<?>> ENTITY_CONDITIONS = DeferredRegister.create(ClassUtil.<EntityCondition<?>>castClass(EntityCondition.class), Apoli.MODID);
	public static final DeferredRegister<ItemCondition<?>> ITEM_CONDITIONS = DeferredRegister.create(ClassUtil.<ItemCondition<?>>castClass(ItemCondition.class), Apoli.MODID);
	public static final DeferredRegister<BlockCondition<?>> BLOCK_CONDITIONS = DeferredRegister.create(ClassUtil.<BlockCondition<?>>castClass(BlockCondition.class), Apoli.MODID);
	public static final DeferredRegister<DamageCondition<?>> DAMAGE_CONDITIONS = DeferredRegister.create(ClassUtil.<DamageCondition<?>>castClass(DamageCondition.class), Apoli.MODID);
	public static final DeferredRegister<FluidCondition<?>> FLUID_CONDITIONS = DeferredRegister.create(ClassUtil.<FluidCondition<?>>castClass(FluidCondition.class), Apoli.MODID);
	public static final DeferredRegister<BiomeCondition<?>> BIOME_CONDITIONS = DeferredRegister.create(ClassUtil.<BiomeCondition<?>>castClass(BiomeCondition.class), Apoli.MODID);
	public static final DeferredRegister<EntityAction<?>> ENTITY_ACTIONS = DeferredRegister.create(ClassUtil.<EntityAction<?>>castClass(EntityAction.class), Apoli.MODID);
	public static final DeferredRegister<ItemAction<?>> ITEM_ACTIONS = DeferredRegister.create(ClassUtil.<ItemAction<?>>castClass(ItemAction.class), Apoli.MODID);
	public static final DeferredRegister<BlockAction<?>> BLOCK_ACTIONS = DeferredRegister.create(ClassUtil.<BlockAction<?>>castClass(BlockAction.class), Apoli.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Apoli.MODID);

	public static void register() {
		ApoliRegistries.POWER_FACTORY = POWER_FACTORIES.makeRegistry("power_factory", () -> new RegistryBuilder<PowerFactory<?>>().disableSaving());
		ApoliRegistries.ENTITY_CONDITION = ENTITY_CONDITIONS.makeRegistry("entity_condition", () -> new RegistryBuilder<EntityCondition<?>>().disableSaving());
		ApoliRegistries.ITEM_CONDITION = ITEM_CONDITIONS.makeRegistry("item_condition", () -> new RegistryBuilder<ItemCondition<?>>().disableSaving());
		ApoliRegistries.BLOCK_CONDITION = BLOCK_CONDITIONS.makeRegistry("block_condition", () -> new RegistryBuilder<BlockCondition<?>>().disableSaving());
		ApoliRegistries.DAMAGE_CONDITION = DAMAGE_CONDITIONS.makeRegistry("damage_condition", () -> new RegistryBuilder<DamageCondition<?>>().disableSaving());
		ApoliRegistries.FLUID_CONDITION = FLUID_CONDITIONS.makeRegistry("fluid_condition", () -> new RegistryBuilder<FluidCondition<?>>().disableSaving());
		ApoliRegistries.BIOME_CONDITION = BIOME_CONDITIONS.makeRegistry("biome_condition", () -> new RegistryBuilder<BiomeCondition<?>>().disableSaving());
		ApoliRegistries.ENTITY_ACTION = ENTITY_ACTIONS.makeRegistry("entity_action", () -> new RegistryBuilder<EntityAction<?>>().disableSaving());
		ApoliRegistries.ITEM_ACTION = ITEM_ACTIONS.makeRegistry("item_action", () -> new RegistryBuilder<ItemAction<?>>().disableSaving());
		ApoliRegistries.BLOCK_ACTION = BLOCK_ACTIONS.makeRegistry("block_action", () -> new RegistryBuilder<BlockAction<?>>().disableSaving());

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		POWER_FACTORIES.register(bus);
		ENTITY_CONDITIONS.register(bus);
		ITEM_CONDITIONS.register(bus);
		BLOCK_CONDITIONS.register(bus);
		DAMAGE_CONDITIONS.register(bus);
		FLUID_CONDITIONS.register(bus);
		BIOME_CONDITIONS.register(bus);
		ENTITY_ACTIONS.register(bus);
		ITEM_ACTIONS.register(bus);
		BLOCK_ACTIONS.register(bus);
		RECIPE_SERIALIZERS.register(bus);

		RECIPE_SERIALIZERS.register("power_restricted", () -> PowerRestrictedCraftingRecipe.SERIALIZER);
		bus.addListener(ApoliRegisters::registerCapabilities);
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IPowerContainer.class);
		event.register(IPowerDataCache.class);
	}
}
