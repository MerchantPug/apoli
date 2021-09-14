package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class PreventBlockActionPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>> {
	public static boolean isSelectionPrevented(Entity entity, BlockPos pos) {
		BlockInWorld position = new BlockInWorld(entity.level, pos, true);
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_BLOCK_SELECTION.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, position));
	}

	public static boolean isUsagePrevented(Entity entity, BlockPos pos) {
		BlockInWorld position = new BlockInWorld(entity.level, pos, true);
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_BLOCK_USAGE.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, position));
	}

	public PreventBlockActionPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredBlockCondition.CODEC, "block_condition"));
	}

	public boolean doesPrevent(ConfiguredPower<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>, ?> configuration, BlockInWorld position) {
		return configuration.getConfiguration().value().map(x -> x.check(position)).orElse(true);
	}
}
