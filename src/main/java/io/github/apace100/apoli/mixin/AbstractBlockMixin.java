package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyBreakSpeedPower;
import io.github.apace100.apoli.power.PreventBlockSelectionPower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin {

    @Inject(at = @At("RETURN"), method = "calcBlockBreakingDelta", cancellable = true)
    private void modifyBlockBreakSpeed(BlockState state, Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> info) {
        float base = info.getReturnValue();
        float modified = PowerHolderComponent.modify(player, ModifyBreakSpeedPower.class, base, p -> p.doesApply(player.level, pos));
        info.setReturnValue(modified);
    }

    @Inject(at = @At("RETURN"), method = "getOutlineShape", cancellable = true)
    private void modifyBlockOutline(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if(context instanceof EntityCollisionContext) {
            if(((EntityCollisionContext)context).getEntity().isPresent()) {
                Entity entity = ((EntityCollisionContext)context).getEntity().get();
                if(PowerHolderComponent.getPowers(entity, PreventBlockSelectionPower.class).stream().anyMatch(p -> p.doesPrevent(entity.level, pos))) {
                    cir.setReturnValue(Shapes.empty());
                }
            }
        }

    }
}
