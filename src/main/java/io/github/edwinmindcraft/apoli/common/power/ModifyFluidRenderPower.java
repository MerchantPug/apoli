package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.apoli.ApoliClient;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredFluidCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyBlockRenderConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFluidRenderConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class ModifyFluidRenderPower extends PowerFactory<ModifyFluidRenderConfiguration> {

	public static Optional<FluidState> getRenderState(Entity entity, LevelReader world, BlockPos pos) {
		return IPowerContainer.getPowers(entity, ApoliPowers.MODIFY_FLUID_RENDER.get()).stream().filter(x -> x.getFactory().check(x, world, pos)).map(x -> x.getConfiguration().fluid().defaultFluidState()).findFirst();
	}

	public ModifyFluidRenderPower() {
		super(ModifyFluidRenderConfiguration.CODEC, false);
	}

	public boolean check(ConfiguredPower<ModifyFluidRenderConfiguration, ?> power, LevelReader world, BlockPos pos) {
		return ConfiguredBlockCondition.check(power.getConfiguration().blockCondition(), world, pos) && ConfiguredFluidCondition.check(power.getConfiguration().fluidCondition(), world.getFluidState(pos));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void onAdded(ModifyFluidRenderConfiguration configuration, Entity entity) {
		ApoliClient.shouldReloadWorldRenderer = true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void onRemoved(ModifyFluidRenderConfiguration configuration, Entity entity) {
		ApoliClient.shouldReloadWorldRenderer = true;
	}
}
