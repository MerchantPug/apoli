package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.access.ModifiableFoodEntity;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFoodConfiguration;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
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

	@Shadow
	private int foodLevel;
	@Shadow
	private float saturationLevel;
	@Unique
	private Player player;

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"))
	private int modifyHunger(FoodProperties foodComponent, Item item, ItemStack stack) {
		int nutrition = foodComponent.getNutrition();
		if (this.player != null) {
			int change = (int) ModifyFoodPower.apply(((ModifiableFoodEntity) this.player).getCurrentModifyFoodPowers(), this.player.level, stack, nutrition, ModifyFoodConfiguration::foodModifiers);
			if (change != nutrition)
				this.apoli$ShouldUpdateManually = true;
			return change;
		}
		return nutrition;
	}

	@Unique
	private boolean apoli$ShouldUpdateManually = false;

	@Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getSaturationModifier()F"))
	private float modifySaturation(FoodProperties foodComponent, Item item, ItemStack stack) {
		float saturationModifier = foodComponent.getSaturationModifier();
		if (this.player != null) {
			float change = (float) ModifyFoodPower.apply(((ModifiableFoodEntity) this.player).getCurrentModifyFoodPowers(), this.player.level, stack, saturationModifier, ModifyFoodConfiguration::saturationModifiers);
			if (change != saturationModifier)
				this.apoli$ShouldUpdateManually = true;
			return change;
		}
		return saturationModifier;
	}

	@Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", shift = At.Shift.AFTER))
	private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) {
		if (this.player != null) {
			ModifyFoodPower.execute(((ModifiableFoodEntity) this.player).getCurrentModifyFoodPowers(), this.player, this.player.level, stack);
			if (this.apoli$ShouldUpdateManually && !this.player.level.isClientSide() && this.player instanceof ServerPlayer sp)
				sp.connection.send(new ClientboundSetHealthPacket(this.player.getHealth(), this.foodLevel, this.saturationLevel));
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void cachePlayer(Player player, CallbackInfo ci) {
		this.player = player;
	}
}
