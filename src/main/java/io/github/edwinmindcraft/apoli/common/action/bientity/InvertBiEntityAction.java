package io.github.edwinmindcraft.apoli.common.action.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import net.minecraft.world.entity.Entity;

public class InvertBiEntityAction extends BiEntityAction<FieldConfiguration<ConfiguredBiEntityAction<?, ?>>> {

	public InvertBiEntityAction() {
		super(FieldConfiguration.codec(ConfiguredBiEntityAction.CODEC, "action"));
	}

	@Override
	public void execute(FieldConfiguration<ConfiguredBiEntityAction<?, ?>> configuration, Entity actor, Entity target) {
		configuration.value().execute(target, actor);
	}
}
