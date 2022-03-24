package io.github.edwinmindcraft.apoli.common.registry.action;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.action.meta.NothingConfiguration;
import net.minecraftforge.registries.RegistryObject;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliDynamicRegisters.*;

public class ApoliDefaultActions {

	public static final RegistryObject<ConfiguredBiEntityAction<?, ?>> BIENTITY_DEFAULT = CONFIGURED_BIENTITY_ACTIONS.register(ApoliDynamicRegistries.ACTION_DEFAULT.getPath(), () -> new ConfiguredBiEntityAction<>(ApoliBiEntityActions.NOTHING, new NothingConfiguration<>()));
	public static final RegistryObject<ConfiguredBlockAction<?, ?>> BLOCK_DEFAULT = CONFIGURED_BLOCK_ACTIONS.register(ApoliDynamicRegistries.ACTION_DEFAULT.getPath(), () -> new ConfiguredBlockAction<>(ApoliBlockActions.NOTHING, new NothingConfiguration<>()));
	public static final RegistryObject<ConfiguredEntityAction<?, ?>> ENTITY_DEFAULT = CONFIGURED_ENTITY_ACTIONS.register(ApoliDynamicRegistries.ACTION_DEFAULT.getPath(), () -> new ConfiguredEntityAction<>(ApoliEntityActions.NOTHING, new NothingConfiguration<>()));
	public static final RegistryObject<ConfiguredItemAction<?, ?>> ITEM_DEFAULT = CONFIGURED_ITEM_ACTIONS.register(ApoliDynamicRegistries.ACTION_DEFAULT.getPath(), () -> new ConfiguredItemAction<>(ApoliItemActions.NOTHING, new NothingConfiguration<>()));

	public static void bootstrap() {

	}
}
