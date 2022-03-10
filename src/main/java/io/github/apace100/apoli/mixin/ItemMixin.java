package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ItemOnItemPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(Item.class)
public class ItemMixin {

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;canAlwaysEat()Z"))
	private boolean makeItemEdible(FoodProperties foodComponent, Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (ModifyFoodPower.isAlwaysEdible(user, world, itemStack))
			return true;
		return foodComponent.isAlwaysEdible();
	}

	@Inject(method = "onClicked", at = @At("RETURN"), cancellable = true)
	private void forgeItem(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			return;
		}
		if (clickType != ClickType.RIGHT) {
			return;
		}
		List<ItemOnItemPower> powers = PowerHolderComponent.getPowers(player, ItemOnItemPower.class).stream().filter(p -> p.doesApply(otherStack, stack)).collect(Collectors.toList());
		for (ItemOnItemPower p :
				powers) {
			p.execute(otherStack, stack, slot);
		}
		if (powers.size() > 0) {
			cir.setReturnValue(true);
		}
	}
}
