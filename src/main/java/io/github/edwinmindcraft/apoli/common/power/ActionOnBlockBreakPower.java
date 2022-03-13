package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnBlockBreakConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ActionOnBlockBreakPower extends PowerFactory<ActionOnBlockBreakConfiguration> {
	public static void execute(Entity player, BlockInWorld pos, boolean successful) {
		IPowerContainer.getPowers(player, ApoliPowers.ACTION_ON_BLOCK_BREAK.get()).stream()
				.filter(p -> p.getFactory().doesApply(p, player, pos))
				.forEach(aobbp -> aobbp.getFactory().executeActions(aobbp, player, successful, pos.getPos(), null));
	}

	public ActionOnBlockBreakPower() {
		super(ActionOnBlockBreakConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredPower<ActionOnBlockBreakConfiguration, ?> config, Entity player, BlockPos pos) {
		return this.doesApply(config, player, new BlockInWorld(player.level, pos, true));
	}

	public boolean doesApply(ConfiguredPower<ActionOnBlockBreakConfiguration, ?> config, Entity player, BlockInWorld cbp) {
		return config.getConfiguration().blockCondition() == null || config.getConfiguration().blockCondition().check(cbp);
	}

	public void executeActions(ConfiguredPower<ActionOnBlockBreakConfiguration, ?> config, Entity player, boolean successfulHarvest, BlockPos pos, Direction dir) {
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
