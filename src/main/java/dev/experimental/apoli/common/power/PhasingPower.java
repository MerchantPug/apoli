package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.PhasingConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class PhasingPower extends PowerFactory<PhasingConfiguration> {
	public static boolean shouldPhaseThrough(LivingEntity entity, CachedBlockPosition position, boolean isAbove) {
		return IPowerContainer.getPowers(entity, ModPowers.PHASING.get()).stream().map(x -> (!isAbove || x.getConfiguration().canPhaseDown(entity)) && x.getConfiguration().canPhaseThrough(position)).anyMatch(Boolean::booleanValue);
	}

	public static boolean shouldPhaseThrough(LivingEntity entity, CachedBlockPosition position) {
		return shouldPhaseThrough(entity, position, false);
	}

	public static boolean shouldPhaseThrough(LivingEntity entity, BlockPos pos) {
		return shouldPhaseThrough(entity, new CachedBlockPosition(entity.world, pos, true), false);
	}

	public static boolean hasRenderMethod(Entity entity, PhasingConfiguration.RenderType renderType) {
		return IPowerContainer.getPowers(entity, ModPowers.PHASING.get()).stream().anyMatch(x -> renderType.equals(x.getConfiguration().renderType()));
	}

	public PhasingPower() {
		super(PhasingConfiguration.CODEC);
	}
}
