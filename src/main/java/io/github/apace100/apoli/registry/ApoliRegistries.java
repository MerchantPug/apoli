package io.github.apace100.apoli.registry;

import com.mojang.serialization.Lifecycle;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.fabric.WrappingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.tuple.Triple;

//TODO Introduce Architectury if possible.
// Probably won't be here.
public class ApoliRegistries {

	public static final Registry<PowerFactory<?>> POWER_FACTORY;
	/*public static final Registry<ConditionFactory<LivingEntity>> ENTITY_CONDITION;
	public static final Registry<ConditionFactory<ItemStack>> ITEM_CONDITION;
	public static final Registry<ConditionFactory<BlockInWorld>> BLOCK_CONDITION;
	public static final Registry<ConditionFactory<Tuple<DamageSource, Float>>> DAMAGE_CONDITION;
	public static final Registry<ConditionFactory<FluidState>> FLUID_CONDITION;
	public static final Registry<ConditionFactory<Biome>> BIOME_CONDITION;*/
	/*public static final Registry<ActionFactory<Entity>> ENTITY_ACTION;
	public static final Registry<ActionFactory<ItemStack>> ITEM_ACTION;
	public static final Registry<ActionFactory<Triple<Level, BlockPos, Direction>>> BLOCK_ACTION;*/

	static {
		POWER_FACTORY = new WrappingRegistry<>(ResourceKey.createRegistryKey(Apoli.identifier("power_factory")), Lifecycle.experimental(), io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries.POWER_FACTORY, io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries.POWER_FACTORY_CLASS, PowerFactory::getWrapped, io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory::getLegacyFactory);
		/*ENTITY_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<LivingEntity>>castClass(ConditionFactory.class), Apoli.identifier("entity_condition")).buildAndRegister();
		ITEM_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<ItemStack>>castClass(ConditionFactory.class), Apoli.identifier("item_condition")).buildAndRegister();
		BLOCK_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<BlockInWorld>>castClass(ConditionFactory.class), Apoli.identifier("block_condition")).buildAndRegister();
		DAMAGE_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<Tuple<DamageSource, Float>>>castClass(ConditionFactory.class), Apoli.identifier("damage_condition")).buildAndRegister();
		FLUID_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<FluidState>>castClass(ConditionFactory.class), Apoli.identifier("fluid_condition")).buildAndRegister();
		BIOME_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<Biome>>castClass(ConditionFactory.class), Apoli.identifier("biome_condition")).buildAndRegister();*/
		//ENTITY_ACTION = new WrappingRegistry<ActionFactory<Entity>, EntityAction<?>>(ResourceKey.createRegistryKey(Apoli.identifier("entity_action")), Lifecycle.experimental(), io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries.ENTITY_ACTION, io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries.ENTITY_ACTION_CLASS, af -> af.getWrapped(EntityAction.CODEC, (serializableData, instanceEntityBiConsumer) -> )));
		//ITEM_ACTION = FabricRegistryBuilder.createSimple(ClassUtil.<ActionFactory<ItemStack>>castClass(ActionFactory.class), Apoli.identifier("item_action")).buildAndRegister();
		//BLOCK_ACTION = FabricRegistryBuilder.createSimple(ClassUtil.<ActionFactory<Triple<Level, BlockPos, Direction>>>castClass(ActionFactory.class), Apoli.identifier("block_action")).buildAndRegister();
	}
}
