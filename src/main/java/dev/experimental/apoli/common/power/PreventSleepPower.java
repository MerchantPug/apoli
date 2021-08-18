package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.PreventSleepConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PreventSleepPower extends PowerFactory<PreventSleepConfiguration> {

	public static boolean tryPreventSleep(Player player, Level world, BlockPos pos) {
		BlockInWorld cbp = new BlockInWorld(world, pos, true);
		boolean flag = false;
		for (ConfiguredPower<PreventSleepConfiguration, PreventSleepPower> p : IPowerContainer.getPowers(player, ModPowers.PREVENT_SLEEP.get())) {
			if (p.getFactory().doesPrevent(p, cbp)) {
				if (p.getConfiguration().allowSpawn() && player instanceof ServerPlayer spe)
					spe.setRespawnPosition(world.dimension(), pos, spe.getYRot(), false, true);
				player.displayClientMessage(new TranslatableComponent(p.getConfiguration().message()), true);
				flag = true;
			}
		}
		return flag;
	}

	public PreventSleepPower() {
		super(PreventSleepConfiguration.CODEC);
	}

	public boolean doesPrevent(ConfiguredPower<PreventSleepConfiguration, ?> configuration, BlockInWorld cbp) {
		return ConfiguredBlockCondition.check(configuration.getConfiguration().condition(), cbp);
	}
}
