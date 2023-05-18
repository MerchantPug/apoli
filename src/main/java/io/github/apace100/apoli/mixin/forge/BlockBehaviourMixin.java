package io.github.apace100.apoli.mixin.forge;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    /*
    Origins Fabric additionally injects into the clause of a -1.0F BlockState.getDestroySpeed.
    Whereas here we use Forge's break speed events, so a modify variable to modify the -1.0 into a 0.0, so it won't
    automatically return 0.0 will do.
    */
    @ModifyVariable(method = "getDestroyProgress", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private float allowUnbreakableBreaking(float value, BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (value == -1.0F && IPowerContainer.get(pPlayer).resolve().isPresent() && IPowerContainer.get(pPlayer).resolve().get().getPowers(ApoliPowers.MODIFY_BREAK_SPEED.get()).stream().anyMatch(p -> ConfiguredBlockCondition.check(p.value().getConfiguration().condition(), pPlayer.level, pPos, () -> pState)))
            return 0.0F;
        return value;
    }
}
