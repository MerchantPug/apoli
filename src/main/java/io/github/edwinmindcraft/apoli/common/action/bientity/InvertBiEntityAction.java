package io.github.edwinmindcraft.apoli.common.action.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.HolderConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import net.minecraft.world.entity.Entity;

public class InvertBiEntityAction extends BiEntityAction<HolderConfiguration<ConfiguredBiEntityAction<?, ?>>> {

	public InvertBiEntityAction() {
		super(HolderConfiguration.required(ConfiguredBiEntityAction.required("action")));
	}

	@Override
	public void execute(HolderConfiguration<ConfiguredBiEntityAction<?, ?>> configuration, Entity actor, Entity target) {
		configuration.holder().value().execute(target, actor);
	}
}
