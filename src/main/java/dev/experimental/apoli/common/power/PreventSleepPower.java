package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.PreventSleepConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PreventSleepPower extends PowerFactory<PreventSleepConfiguration> {

	public static boolean tryPreventSleep(Player player, Level world, BlockPos pos) {
		CachedBlockPosition cbp = new CachedBlockPosition(world, pos, true);
		boolean flag = false;
		for (ConfiguredPower<PreventSleepConfiguration, PreventSleepPower> p : IPowerContainer.getPowers(player, ModPowers.PREVENT_SLEEP.get())) {
			if (p.getFactory().doesPrevent(p, cbp)) {
				if (p.getConfiguration().allowSpawn() && player instanceof ServerPlayerEntity spe)
					spe.setSpawnPoint(world.getRegistryKey(), pos, spe.getYaw(), false, true);
				player.sendMessage(new TranslatableText(p.getConfiguration().message()), true);
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
