package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemMixin {

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;canAlwaysEat()Z"))
	private boolean makeItemEdible(FoodProperties foodComponent, Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (ModifyFoodPower.isAlwaysEdible(user, world, itemStack))
			return true;
		return foodComponent.canAlwaysEat();
	}
}
