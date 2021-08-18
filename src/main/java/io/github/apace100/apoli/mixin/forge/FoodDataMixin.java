package io.github.apace100.apoli.mixin.forge;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoodData.class)
public class FoodDataMixin {
	/**
	 * Was moved here from PlayerEntityMixin, as the method is called isHurt on mojmap,
	 * which means more modders might use it.<br/>
	 * Injecting it here is, in my opinion, safer.
	 */
	@ModifyVariable(method = "tick", at = @At(value = "STORE", ordinal = 0))
	public boolean preventNaturalRegeneration(boolean bool, Player player) {
		if (IPowerContainer.hasPower(player, ModPowers.DISABLE_REGEN.get()))
			return false;
		return bool;
	}
}
