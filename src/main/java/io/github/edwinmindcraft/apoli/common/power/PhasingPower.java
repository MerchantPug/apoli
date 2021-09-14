package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.PhasingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.Optional;

public class PhasingPower extends PowerFactory<PhasingConfiguration> {
	public static boolean shouldPhaseThrough(LivingEntity entity, BlockInWorld position, boolean isAbove) {
		return IPowerContainer.getPowers(entity, ModPowers.PHASING.get()).stream().anyMatch(x -> (!isAbove || x.getConfiguration().canPhaseDown(entity)) && x.getConfiguration().canPhaseThrough(position));
	}

	public static boolean shouldPhaseThrough(LivingEntity entity, BlockInWorld position) {
		return shouldPhaseThrough(entity, position, false);
	}

	public static boolean shouldPhaseThrough(LivingEntity entity, BlockPos pos) {
		return shouldPhaseThrough(entity, new BlockInWorld(entity.level, pos, true), false);
	}

	public static boolean hasRenderMethod(Entity entity, PhasingConfiguration.RenderType renderType) {
		return IPowerContainer.getPowers(entity, ModPowers.PHASING.get()).stream().anyMatch(x -> renderType.equals(x.getConfiguration().renderType()));
	}
	public static Optional<Float> getRenderMethod(Entity entity, PhasingConfiguration.RenderType renderType) {
		return IPowerContainer.getPowers(entity, ModPowers.PHASING.get()).stream().filter(x -> renderType.equals(x.getConfiguration().renderType())).map(x -> x.getConfiguration().viewDistance()).min(Float::compareTo);
	}

	public PhasingPower() {
		super(PhasingConfiguration.CODEC);
	}
}
