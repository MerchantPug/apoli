package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.PhasingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.common.util.NonNullSupplier;

import java.util.Optional;
import java.util.function.Supplier;

public class PhasingPower extends PowerFactory<PhasingConfiguration> {
	public static boolean shouldPhaseThrough(Entity entity, LevelReader reader, BlockPos pos, NonNullSupplier<BlockState> stateGetter, boolean isAbove) {
		return IPowerContainer.getPowers(entity, ApoliPowers.PHASING.get()).stream().anyMatch(x -> (!isAbove || x.getConfiguration().canPhaseDown(entity)) && x.getConfiguration().canPhaseThrough(reader, pos, stateGetter));
	}

	public static boolean shouldPhaseThrough(Entity entity, LevelReader reader, BlockPos pos, NonNullSupplier<BlockState> stateGetter) {
		return shouldPhaseThrough(entity, reader, pos, stateGetter, false);
	}

	public static boolean shouldPhaseThrough(Entity entity, BlockPos pos) {
		return shouldPhaseThrough(entity, entity.level, pos, () -> entity.getLevel().getBlockState(pos));
	}

	public static boolean hasRenderMethod(Entity entity, PhasingConfiguration.RenderType renderType) {
		return IPowerContainer.getPowers(entity, ApoliPowers.PHASING.get()).stream().anyMatch(x -> renderType.equals(x.getConfiguration().renderType()));
	}

	public static Optional<Float> getRenderMethod(Entity entity, PhasingConfiguration.RenderType renderType) {
		return IPowerContainer.getPowers(entity, ApoliPowers.PHASING.get()).stream().filter(x -> renderType.equals(x.getConfiguration().renderType())).map(x -> x.getConfiguration().viewDistance()).min(Float::compareTo);
	}


	public PhasingPower() {
		super(PhasingConfiguration.CODEC);
	}
}
