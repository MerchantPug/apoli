package io.github.apace100.apoli.mixin;

/* We do not need this class as Forge conveniently changes it so that any item is
allowed to be put into a grindstone's repair slots.
@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$2")
public class GrindstoneScreenHandlerTopInputSlotMixin {

    @Final
    @Shadow
    GrindstoneMenu this$0;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void allowPowerStacks(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        PowerModifiedGrindstone pmg = (PowerModifiedGrindstone) this$0;
        if (ModifyGrindstonePower.checkTop(pmg.getPlayer(), stack)) {
            cir.setReturnValue(true);
        }
    }
}
 */
