package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.ModifiableFoodEntity;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFoodConfiguration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerManagerMixin {

	@Unique
	private Player player;
    @Shadow private int foodLevel;
    @Shadow private float foodSaturationLevel;
    @Unique
    private PlayerEntity player;

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"))
	private int modifyHunger(FoodProperties foodComponent, Item item, ItemStack stack) {
		if (this.player != null) {
			return (int) ModifyFoodPower.apply(this.player, stack, foodComponent.getNutrition(), ModifyFoodConfiguration::foodModifiers);
		}
		return foodComponent.getNutrition();
	}
    @Unique
    private boolean apoli$ShouldUpdateManually = false;

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/FoodComponent;getHunger()I"))
    private int modifyHunger(FoodComponent foodComponent, Item item, ItemStack stack) {
        apoli$ShouldUpdateManually = false;
        if(player != null) {
            double baseValue = foodComponent.getHunger();
        List<EntityAttributeModifier> modifiers = ((ModifiableFoodEntity)player).getCurrentModifyFoodPowers().stream()
            .filter(p -> p.doesApply(stack))
            .flatMap(p -> p.getFoodModifiers().stream()).collect(Collectors.toList());
            int newFood = (int) AttributeUtil.sortAndApplyModifiers(modifiers, baseValue);
            if(newFood != (int)baseValue && newFood == 0) {
                apoli$ShouldUpdateManually = true;
            }
            return newFood;
        }
        return foodComponent.getHunger();
    }

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getSaturationModifier()F"))
	private float modifySaturation(FoodProperties foodComponent, Item item, ItemStack stack) {
		if (this.player != null)
			return (float) ModifyFoodPower.apply(this.player, stack, foodComponent.getSaturationModifier(), ModifyFoodConfiguration::saturationModifiers);
		return foodComponent.getSaturationModifier();
	}

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/FoodComponent;getSaturationModifier()F"))
    private float modifySaturation(FoodComponent foodComponent, Item item, ItemStack stack) {
        if(player != null) {
            double baseValue = foodComponent.getSaturationModifier();
            List<EntityAttributeModifier> modifiers = ((ModifiableFoodEntity)player).getCurrentModifyFoodPowers().stream()
                .filter(p -> p.doesApply(stack))
                .flatMap(p -> p.getSaturationModifiers().stream()).collect(Collectors.toList());
            float newSaturation = (float) AttributeUtil.sortAndApplyModifiers(modifiers, baseValue);
            if(newSaturation != baseValue && newSaturation == 0) {
                apoli$ShouldUpdateManually = true;
            }
            return newSaturation;
        }
        return foodComponent.getSaturationModifier();
    }

	@Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", shift = At.Shift.AFTER))
	private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) {
		if (this.player != null) {
            ModifyFoodPower.execute(this.player, stack);
		}
	}
    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V", shift = At.Shift.AFTER))
    private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) {
        if(player != null) {
            ((ModifiableFoodEntity)player).getCurrentModifyFoodPowers().stream().filter(p -> p.doesApply(stack)).forEach(ModifyFoodPower::eat);
            if(apoli$ShouldUpdateManually && !player.world.isClient) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(new HealthUpdateS2CPacket(player.getHealth(), foodLevel, foodSaturationLevel));
            }
        }
    }

	@Inject(method = "tick", at = @At("HEAD"))
	private void cachePlayer(Player player, CallbackInfo ci) {
		this.player = player;
	}
}
