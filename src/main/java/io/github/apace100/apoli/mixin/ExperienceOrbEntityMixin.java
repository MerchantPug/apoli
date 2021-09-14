package io.github.apace100.apoli.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbEntityMixin {

	@Shadow
	public int value;

	@Inject(method = "playerTouch", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;takeXpDelay:I", ordinal = 1))
	private void modifyXpAmount(Player player, CallbackInfo ci) {
		this.value = (int) IPowerContainer.modify(player, ModPowers.MODIFY_EXPERIENCE.get(), this.value);
	}
}
