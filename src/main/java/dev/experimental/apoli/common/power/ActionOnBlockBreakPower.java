package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ActionOnBlockBreakConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ActionOnBlockBreakPower extends PowerFactory<ActionOnBlockBreakConfiguration> {
	public static void execute(LivingEntity player, CachedBlockPosition pos, boolean successful) {
		IPowerContainer.getPowers(player, ModPowers.ACTION_ON_BLOCK_BREAK.get()).stream()
				.filter(p -> p.getFactory().doesApply(p, player, pos))
				.forEach(aobbp -> aobbp.getFactory().executeActions(aobbp, player, successful, pos.getBlockPos(), null));
	}

	public ActionOnBlockBreakPower() {
		super(ActionOnBlockBreakConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, BlockPos pos) {
		return this.doesApply(config, player, new CachedBlockPosition(player.world, pos, true));
	}

	public boolean doesApply(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, CachedBlockPosition cbp) {
		return config.getConfiguration().blockCondition() == null || config.getConfiguration().blockCondition().check(cbp);
	}

	public void executeActions(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, boolean successfulHarvest, BlockPos pos, Direction dir) {
		ActionOnBlockBreakConfiguration configuration = config.getConfiguration();
		if (successfulHarvest || !configuration.onlyWhenHarvested()) {
			if (configuration.blockAction() != null) {
				configuration.blockAction().execute(player.world, pos, dir);
			}
			if (configuration.entityAction() != null) {
				configuration.entityAction().execute(player);
			}
		}
	}
}
