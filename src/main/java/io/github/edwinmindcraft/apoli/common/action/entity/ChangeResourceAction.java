package io.github.edwinmindcraft.apoli.common.action.entity;

import com.mojang.serialization.Codec;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.ChangeResourceConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ChangeResourceAction extends EntityAction<ChangeResourceConfiguration> {
	public ChangeResourceAction(Codec<ChangeResourceConfiguration> codec) {
		super(codec);
	}

	@Override
	public void execute(ChangeResourceConfiguration configuration, Entity entity) {
		if (entity instanceof Player player) {
			ConfiguredPower<?, ?> power = IPowerContainer.get(player).resolve().map(x -> x.getPower(configuration.resource())).orElse(null);
			if (power != null) {
				if (configuration.operation() == ResourceOperation.ADD)
					power.change(player, configuration.amount());
				else if (configuration.operation() == ResourceOperation.SET)
					power.assign(player, configuration.amount());
				ApoliAPI.synchronizePowerContainer(player);
			}
		}
	}
}
