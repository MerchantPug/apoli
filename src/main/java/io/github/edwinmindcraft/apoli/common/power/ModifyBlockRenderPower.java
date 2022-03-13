package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.apoli.ApoliClient;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyBlockRenderConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class ModifyBlockRenderPower extends PowerFactory<ModifyBlockRenderConfiguration> {

	public static Optional<BlockState> getRenderState(Entity entity, LevelReader world, BlockPos pos) {
		return IPowerContainer.getPowers(entity, ApoliPowers.MODIFY_BLOCK_RENDER.get()).stream().filter(x -> x.getFactory().check(x, world, pos)).map(x -> x.getConfiguration().block().defaultBlockState()).findFirst();
	}

	public ModifyBlockRenderPower() {
		super(ModifyBlockRenderConfiguration.CODEC, false);
	}

	public boolean check(ConfiguredPower<ModifyBlockRenderConfiguration, ?> power, LevelReader world, BlockPos pos) {
		BlockInWorld cbp = new BlockInWorld(world, pos, true);
		return ConfiguredBlockCondition.check(power.getConfiguration().blockCondition(), cbp);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void onAdded(ModifyBlockRenderConfiguration configuration, Entity entity) {
		ApoliClient.shouldReloadWorldRenderer = true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void onRemoved(ModifyBlockRenderConfiguration configuration, Entity entity) {
		ApoliClient.shouldReloadWorldRenderer = true;
	}
}
