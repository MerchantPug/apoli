package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyFoodPower;
import io.github.apace100.apoli.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(FoodData.class)
public class HungerManagerMixin {

    @Unique
    private Player player;

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/FoodComponent;getHunger()I"))
    private int modifyHunger(FoodProperties foodComponent, Item item, ItemStack stack) {
        if(player != null) {
            double baseValue = foodComponent.getNutrition();
        List<AttributeModifier> modifiers = PowerHolderComponent.KEY.get(player).getPowers(ModifyFoodPower.class).stream()
            .filter(p -> p.doesApply(stack))
            .flatMap(p -> p.getFoodModifiers().stream()).collect(Collectors.toList());
            return (int) AttributeUtil.sortAndApplyModifiers(modifiers, baseValue);
        }
        return foodComponent.getNutrition();
    }

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/FoodComponent;getSaturationModifier()F"))
    private float modifySaturation(FoodProperties foodComponent, Item item, ItemStack stack) {
        if(player != null) {
            double baseValue = foodComponent.getSaturationModifier();
            List<AttributeModifier> modifiers = PowerHolderComponent.KEY.get(player).getPowers(ModifyFoodPower.class).stream()
                .filter(p -> p.doesApply(stack))
                .flatMap(p -> p.getSaturationModifiers().stream()).collect(Collectors.toList());
            return (float) AttributeUtil.sortAndApplyModifiers(modifiers, baseValue);
        }
        return foodComponent.getSaturationModifier();
    }

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V", shift = At.Shift.AFTER))
    private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) {
        if(player != null) {
            PowerHolderComponent.KEY.get(player).getPowers(ModifyFoodPower.class).stream().filter(p -> p.doesApply(stack)).forEach(ModifyFoodPower::eat);
        }
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void cachePlayer(Player player, CallbackInfo ci) {
        this.player = player;
    }
}
