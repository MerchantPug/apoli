package io.github.apace100.apoli.mixin.forge;

import io.github.edwinmindcraft.apoli.common.power.ClimbingPower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

	@Inject(method = "isLivingOnLadder", remap = false, at = @At("RETURN"), cancellable = true)
	private static void ladder(BlockState state, Level world, BlockPos pos, LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValue() && ClimbingPower.check(entity, blockPos -> {}))
			info.setReturnValue(true);
	}
}
