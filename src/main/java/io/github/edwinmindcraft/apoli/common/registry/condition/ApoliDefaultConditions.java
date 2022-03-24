package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraftforge.registries.RegistryObject;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliDynamicRegisters.*;

public class ApoliDefaultConditions {
	public static final RegistryObject<ConfiguredBiEntityCondition<?, ?>> BIENTITY_DEFAULT = CONFIGURED_BIENTITY_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredBiEntityCondition<>(ApoliBiEntityConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredBiomeCondition<?, ?>> BIOME_DEFAULT = CONFIGURED_BIOME_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredBiomeCondition<>(ApoliBiomeConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredBlockCondition<?, ?>> BLOCK_DEFAULT = CONFIGURED_BLOCK_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredBlockCondition<>(ApoliBlockConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredDamageCondition<?, ?>> DAMAGE_DEFAULT = CONFIGURED_DAMAGE_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredDamageCondition<>(ApoliDamageConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredEntityCondition<?, ?>> ENTITY_DEFAULT = CONFIGURED_ENTITY_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredEntityCondition<>(ApoliEntityConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredFluidCondition<?, ?>> FLUID_DEFAULT = CONFIGURED_FLUID_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredFluidCondition<>(ApoliFluidConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredItemCondition<?, ?>> ITEM_DEFAULT = CONFIGURED_ITEM_CONDITIONS.register(ApoliDynamicRegistries.CONDITION_DEFAULT.getPath(), () -> new ConfiguredItemCondition<>(ApoliItemConditions.CONSTANT, new ConstantConfiguration<>(true), ConditionData.DEFAULT));

	public static final RegistryObject<ConfiguredBiEntityCondition<?, ?>> BIENTITY_DENY = CONFIGURED_BIENTITY_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredBiEntityCondition<>(ApoliBiEntityConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredBiomeCondition<?, ?>> BIOME_DENY = CONFIGURED_BIOME_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredBiomeCondition<>(ApoliBiomeConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredBlockCondition<?, ?>> BLOCK_DENY = CONFIGURED_BLOCK_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredBlockCondition<>(ApoliBlockConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredDamageCondition<?, ?>> DAMAGE_DENY = CONFIGURED_DAMAGE_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredDamageCondition<>(ApoliDamageConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredEntityCondition<?, ?>> ENTITY_DENY = CONFIGURED_ENTITY_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredEntityCondition<>(ApoliEntityConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredFluidCondition<?, ?>> FLUID_DENY = CONFIGURED_FLUID_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredFluidCondition<>(ApoliFluidConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));
	public static final RegistryObject<ConfiguredItemCondition<?, ?>> ITEM_DENY = CONFIGURED_ITEM_CONDITIONS.register(ApoliDynamicRegistries.DENY.getPath(), () -> new ConfiguredItemCondition<>(ApoliItemConditions.CONSTANT, new ConstantConfiguration<>(false), ConditionData.DEFAULT));

	public static void bootstrap() {

	}
}
