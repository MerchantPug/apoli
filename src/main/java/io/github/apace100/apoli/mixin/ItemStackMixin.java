package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ActionOnItemUsePower;
import io.github.apace100.apoli.power.PreventItemUsePower;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info) {
        if(user != null) {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
            ItemStack stackInHand = user.getItemInHand(hand);
            for(PreventItemUsePower piup : component.getPowers(PreventItemUsePower.class)) {
                if(piup.doesPrevent(stackInHand)) {
                    info.setReturnValue(InteractionResultHolder.fail(stackInHand));
                    break;
                }
            }
        }
    }

    @Unique
    private ItemStack usedItemStack;

    @Inject(method = "finishUsing", at = @At("HEAD"))
    public void cacheUsedItemStack(Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        usedItemStack = ((ItemStack)(Object)this).copy();
    }

    @Inject(method = "finishUsing", at = @At("RETURN"))
    public void callActionOnUse(Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if(user instanceof Player) {
            ItemStack returnStack = cir.getReturnValue();
            PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
            for(ActionOnItemUsePower p : component.getPowers(ActionOnItemUsePower.class)) {
                if(p.doesApply(usedItemStack)) {
                    p.executeActions(returnStack);
                }
            }
        }
    }
}
