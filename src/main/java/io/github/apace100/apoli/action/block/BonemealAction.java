package io.github.apace100.apoli.action.block;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BonemealAction extends BlockAction<FieldConfiguration<Boolean>> {

	public BonemealAction() {
		super(FieldConfiguration.codec(Codec.BOOL, "effects", true));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(@NotNull FieldConfiguration<Boolean> configuration, @NotNull Level world, @NotNull BlockPos pos, @NotNull Direction direction) {
		BlockPos blockPos2 = pos.relative(direction);

		boolean spawnEffects = configuration.value();

		if (BoneMealItem.growCrop(ItemStack.EMPTY, world, pos)) { //Use the fake player version (Mostly because I'm lazy)
			if (spawnEffects && !world.isClientSide()) {
				world.globalLevelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
			}
		} else {
			BlockState blockState = world.getBlockState(pos);
			boolean bl = blockState.isFaceSturdy(world, pos, direction);
			if (bl && BoneMealItem.growWaterPlant(ItemStack.EMPTY, world, blockPos2, direction)) {
				if (spawnEffects && !world.isClientSide()) {
					world.globalLevelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos2, 0);
				}
			}
		}
	}
}
