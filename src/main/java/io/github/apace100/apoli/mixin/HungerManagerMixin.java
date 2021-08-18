package io.github.apace100.apoli.mixin;

import dev.experimental.apoli.common.power.ModifyFoodPower;
import dev.experimental.apoli.common.power.configuration.ModifyFoodConfiguration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerManagerMixin {

	@Unique
	private Player player;

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"))
	private int modifyHunger(FoodProperties foodComponent, Item item, ItemStack stack) {
		if (this.player != null) {
			return (int) ModifyFoodPower.apply(this.player, stack, foodComponent.getNutrition(), ModifyFoodConfiguration::foodModifiers);
		}
		return foodComponent.getNutrition();
	}

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getSaturationModifier()F"))
	private float modifySaturation(FoodProperties foodComponent, Item item, ItemStack stack) {
		if (this.player != null)
			return (float) ModifyFoodPower.apply(this.player, stack, foodComponent.getSaturationModifier(), ModifyFoodConfiguration::saturationModifiers);
		return foodComponent.getSaturationModifier();
	}

	@Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", shift = At.Shift.AFTER))
	private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) {
		if (this.player != null) {
            ModifyFoodPower.execute(this.player, stack);
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void cachePlayer(Player player, CallbackInfo ci) {
		this.player = player;
	}
}
