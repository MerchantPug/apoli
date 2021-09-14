package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyHarvestConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import java.util.Optional;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class ModifyHarvestPower extends ValueModifyingPowerFactory<ModifyHarvestConfiguration> {
	/**
	 * This implementation assumes that if a single power says you can harvest, you can harvest.
	 */
	public static Optional<Boolean> isHarvestAllowed(Player player, BlockInWorld position) {
		return IPowerContainer.getPowers(player, ModPowers.MODIFY_HARVEST.get()).stream()
				.filter(x -> x.getFactory().doesApply(x, position))
				.map(x -> x.getFactory().isHarvestAllowed(x))
				.reduce((x, y) -> x || y);
	}

	public ModifyHarvestPower() {
		super(ModifyHarvestConfiguration.CODEC);
	}

	public boolean doesApply(ConfiguredPower<ModifyHarvestConfiguration, ?> config, BlockInWorld pos) {
		return ConfiguredBlockCondition.check(config.getConfiguration().condition(), pos);
	}

	public boolean isHarvestAllowed(ConfiguredPower<ModifyHarvestConfiguration, ?> config) {
		return config.getConfiguration().allow();
	}
}
