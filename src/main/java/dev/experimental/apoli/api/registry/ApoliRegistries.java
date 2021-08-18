package dev.experimental.apoli.api.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.power.factory.*;
import io.github.apace100.calio.ClassUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ApoliRegistries {

	public static final ResourceKey<net.minecraft.core.Registry<PowerFactory<?>>> POWER_FACTORY_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("power_factory"));
	public static final ResourceKey<net.minecraft.core.Registry<EntityCondition<?>>> ENTITY_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("entity_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<ItemCondition<?>>> ITEM_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("item_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<BlockCondition<?>>> BLOCK_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("block_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<DamageCondition<?>>> DAMAGE_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("damage_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<FluidCondition<?>>> FLUID_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("fluid_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<BiomeCondition<?>>> BIOME_CONDITION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("biome_condition"));
	public static final ResourceKey<net.minecraft.core.Registry<EntityAction<?>>> ENTITY_ACTION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("entity_action"));
	public static final ResourceKey<net.minecraft.core.Registry<ItemAction<?>>> ITEM_ACTION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("item_action"));
	public static final ResourceKey<net.minecraft.core.Registry<BlockAction<?>>> BLOCK_ACTION_KEY = ResourceKey.createRegistryKey(ApoliAPI.identifier("block_action"));

	public static Supplier<IForgeRegistry<PowerFactory<?>>> POWER_FACTORY;
	public static Supplier<IForgeRegistry<EntityCondition<?>>> ENTITY_CONDITION;
	public static Supplier<IForgeRegistry<ItemCondition<?>>> ITEM_CONDITION;
	public static Supplier<IForgeRegistry<BlockCondition<?>>> BLOCK_CONDITION;
	public static Supplier<IForgeRegistry<DamageCondition<?>>> DAMAGE_CONDITION;
	public static Supplier<IForgeRegistry<FluidCondition<?>>> FLUID_CONDITION;
	public static Supplier<IForgeRegistry<BiomeCondition<?>>> BIOME_CONDITION;
	public static Supplier<IForgeRegistry<EntityAction<?>>> ENTITY_ACTION;
	public static Supplier<IForgeRegistry<ItemAction<?>>> ITEM_ACTION;
	public static Supplier<IForgeRegistry<BlockAction<?>>> BLOCK_ACTION;

	/**
	 * This is basically {@link net.minecraft.core.Registry}, just altered in such a way that it works with
	 * architectury's registries.
	 *
	 * @param registry The registry to create the codec for.
	 *
	 * @return The new codec.
	 */
	public static <T extends IForgeRegistryEntry<T>> Codec<T> codec(Supplier<IForgeRegistry<T>> registry) {
		Supplier<ForgeRegistry<T>> supplier = () -> (ForgeRegistry<T>) registry.get();
		return new Codec<>() {
			@Override
			public <U> DataResult<Pair<T, U>> decode(DynamicOps<U> dynamicOps, U input) {
				return dynamicOps.compressMaps() ? dynamicOps.getNumberValue(input).flatMap((number) -> {
					T object = supplier.get().getValue(number.intValue());
					return object == null ? DataResult.error("Unknown registry id: " + number) : DataResult.success(object);
				}).map((objectx) -> Pair.of(objectx, dynamicOps.empty())) : ResourceLocation.CODEC.decode(dynamicOps, input).flatMap((pair) -> {
					T object = supplier.get().getValue(pair.getFirst());
					return object == null ? DataResult.error("Unknown registry key: " + pair.getFirst()) : DataResult.success(Pair.of(object, pair.getSecond()));
				});
			}

			@Override
			public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
				ResourceLocation identifier = supplier.get().getKey(input);
				if (identifier == null) {
					return DataResult.error("Unknown registry element " + input);
				} else {
					return ops.compressMaps() ?
							ops.mergeToPrimitive(prefix, ops.createInt(supplier.get().getID(input))) :
							ops.mergeToPrimitive(prefix, ops.createString(identifier.toString()));
				}
			}
		};
	}
}
