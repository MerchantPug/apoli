package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnBlockBreakConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ActionOnBlockBreakPower extends PowerFactory<ActionOnBlockBreakConfiguration> {
	public static void execute(LivingEntity player, BlockInWorld pos, boolean successful) {
		IPowerContainer.getPowers(player, ApoliPowers.ACTION_ON_BLOCK_BREAK.get()).stream()
				.filter(p -> p.getFactory().doesApply(p, player, pos))
				.forEach(aobbp -> aobbp.getFactory().executeActions(aobbp, player, successful, pos.getPos(), null));
	}

	public ActionOnBlockBreakPower() {
		super(ActionOnBlockBreakConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, BlockPos pos) {
		return this.doesApply(config, player, new BlockInWorld(player.level, pos, true));
	}

	public boolean doesApply(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, BlockInWorld cbp) {
		return config.getConfiguration().blockCondition() == null || config.getConfiguration().blockCondition().check(cbp);
	}

	public void executeActions(ConfiguredFactory<ActionOnBlockBreakConfiguration, ?> config, LivingEntity player, boolean successfulHarvest, BlockPos pos, Direction dir) {
		ActionOnBlockBreakConfiguration configuration = config.getConfiguration();
		if (successfulHarvest || !configuration.onlyWhenHarvested()) {
			if (configuration.blockAction() != null) {
				configuration.blockAction().execute(player.level, pos, dir);
			}
			if (configuration.entityAction() != null) {
				configuration.entityAction().execute(player);
			}
		}
	}
}
