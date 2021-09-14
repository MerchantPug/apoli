package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.common.power.PreventItemActionPower;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public class BlockItemMixin {

	@Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;"))
	private InteractionResultHolder<ItemStack> preventItemUseIfBlockItem(BlockItem blockItem, Level world, Player user, InteractionHand hand) {
		if (user != null) {
			ItemStack stackInHand = user.getItemInHand(hand);
			if (PreventItemActionPower.isUsagePrevented(user, stackInHand))
				return InteractionResultHolder.fail(stackInHand);
		}
		return blockItem.use(world, user, hand);
	}
}
