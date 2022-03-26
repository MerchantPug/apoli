package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnBlockBreakConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnWakeUpConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.common.util.NonNullSupplier;

public class ActionOnWakeUpPower extends PowerFactory<ActionOnWakeUpConfiguration> {
	public static void execute(Entity player, BlockPos pos) {
		IPowerContainer.getPowers(player, ApoliPowers.ACTION_ON_WAKE_UP.get()).stream()
				.filter(power -> power.getFactory().doesApply(power, player, player.level, pos))
				.forEach(aobbp -> aobbp.getFactory().executeActions(aobbp, player, pos, Direction.DOWN));
	}

	public ActionOnWakeUpPower() {
		super(ActionOnWakeUpConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredPower<ActionOnWakeUpConfiguration, ?> config, Entity player, LevelReader reader, BlockPos position) {
		return ConfiguredBlockCondition.check(config.getConfiguration().blockCondition(), reader, position);
	}

	public void executeActions(ConfiguredPower<ActionOnWakeUpConfiguration, ?> config, Entity player, BlockPos pos, Direction dir) {
		ActionOnWakeUpConfiguration configuration = config.getConfiguration();
		if (configuration.blockAction() != null)
			configuration.blockAction().execute(player.level, pos, dir);
		if (configuration.entityAction() != null)
			configuration.entityAction().execute(player);
	}
}
