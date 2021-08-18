package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.configuration.PowerReference;
import dev.experimental.apoli.api.power.ICooldownPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TriggerCooldownAction extends EntityAction<PowerReference> {
	public TriggerCooldownAction() {
		super(PowerReference.codec("power"));
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void execute(PowerReference configuration, Entity entity) {
		if (entity instanceof Player player) {
			ConfiguredPower<?, ?> power = ApoliAPI.getPowerContainer(entity).getPower(configuration.power());
			if (power.getFactory() instanceof ICooldownPower cp) {
				cp.use(power, player);
			}
		}
	}
}
