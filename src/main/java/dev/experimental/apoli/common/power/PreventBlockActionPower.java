package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class PreventBlockActionPower extends PowerFactory<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>> {
	public static boolean isSelectionPrevented(Entity entity, BlockPos pos) {
		CachedBlockPosition position = new CachedBlockPosition(entity.world, pos, true);
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_BLOCK_SELECTION.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, position));
	}

	public static boolean isUsagePrevented(Entity entity, BlockPos pos) {
		CachedBlockPosition position = new CachedBlockPosition(entity.world, pos, true);
		return IPowerContainer.getPowers(entity, ModPowers.PREVENT_BLOCK_USAGE.get()).stream().anyMatch(x -> x.getFactory().doesPrevent(x, position));
	}

	public PreventBlockActionPower() {
		super(FieldConfiguration.optionalCodec(ConfiguredBlockCondition.CODEC, "block_condition"));
	}

	public boolean doesPrevent(ConfiguredPower<FieldConfiguration<Optional<ConfiguredBlockCondition<?, ?>>>, ?> configuration, CachedBlockPosition position) {
		return configuration.getConfiguration().value().map(x -> x.check(position)).orElse(true);
	}
}
