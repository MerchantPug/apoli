package io.github.apace100.apoli.mixin;

/*
@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public class GrindstoneScreenHandlerOutputSlotMixin {

    @Final
    @Shadow
    GrindstoneMenu this$0;

    @Inject(method = "onTake", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private void executeGrindstoneActions(Player player, ItemStack stack, CallbackInfo ci) {
        PowerModifiedGrindstone pmg = (PowerModifiedGrindstone) this$0;
        List<Holder<ConfiguredPower<ModifyGrindstoneConfiguration, ModifyGrindstonePower>>> applyingPowers = pmg.getAppliedPowers();
        ModifyGrindstonePower.tryExecute(applyingPowers, pmg.getPlayer(), new MutableObject<>(stack), pmg.getPos());
    }

    @Inject(method = "getExperienceAmount", at = @At("RETURN"), cancellable = true)
    private void modifyExperience(Level world, CallbackInfoReturnable<Integer> cir) {
        PowerModifiedGrindstone pmg = (PowerModifiedGrindstone) this$0;
        if(pmg.getAppliedPowers().isEmpty()) {
            return;
        }
        List<ConfiguredModifier<?>> modifiers = ModifyGrindstonePower.tryGetExperienceModifiers(((PowerModifiedGrindstone) this$0).getAppliedPowers());
        int xp = (int)ModifierUtils.applyModifiers(pmg.getPlayer(), modifiers, cir.getReturnValue());
        cir.setReturnValue(xp);
    }
}
 */