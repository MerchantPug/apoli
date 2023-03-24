package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.ModifyResourceConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ModifyResourceAction extends EntityAction<ModifyResourceConfiguration> {
	public ModifyResourceAction() {
		super(ModifyResourceConfiguration.CODEC);
	}

	@Override
	public void execute(ModifyResourceConfiguration configuration, Entity entity) {
		if (entity instanceof Player player && configuration.resource().isBound()) {
			ConfiguredPower<?, ?> power = configuration.resource().value();
			if (IPowerContainer.get(entity).resolve().flatMap(x -> configuration.resource().unwrapKey().map(x::hasPower)).orElse(false)) {
				power.assign(entity, (int) ModifierUtil.applyModifiers(entity, configuration.modifier().entries(), power.getValue(entity).orElse(0)));
				ApoliAPI.synchronizePowerContainer(player);
			}
		}
	}
}
