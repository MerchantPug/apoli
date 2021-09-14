package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.ChangeResourceConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ChangeResourceAction extends EntityAction<ChangeResourceConfiguration> {
	public ChangeResourceAction() {
		super(ChangeResourceConfiguration.CODEC);
	}

	@Override
	public void execute(ChangeResourceConfiguration configuration, Entity entity) {
		if (entity instanceof Player player) {
			ConfiguredPower<?, ?> power = IPowerContainer.get(player).resolve().map(x -> x.getPower(configuration.resource())).orElse(null);
			if (power != null) {
				power.change(player, configuration.amount());
				ApoliAPI.synchronizePowerContainer(player);
			}
		}
	}
}
