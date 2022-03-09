package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyCameraSubmersionTypePower;
import io.github.edwinmindcraft.apoli.api.power.INightVisionPower;
import io.github.edwinmindcraft.apoli.common.power.LavaVisionPower;
import io.github.edwinmindcraft.apoli.common.power.PhasingPower;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FogRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class BackgroundRendererMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z", ordinal = 1), method = "setupColor")
	private static boolean hasStatusEffectProxy(LivingEntity player, MobEffect effect) {
		if (player instanceof Player && effect == MobEffects.NIGHT_VISION && !player.hasEffect(MobEffects.NIGHT_VISION)) {
			return INightVisionPower.getNightVisionStrength(player).isPresent();
		}
		return player.hasEffect(effect);
	}

	@ModifyVariable(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 0), ordinal = 0)
	private static FogType modifyCameraSubmersionTypeRender(FogType original, Camera camera) {
		if (camera.getEntity() instanceof LivingEntity) {
			for (ModifyCameraSubmersionTypePower p : PowerHolderComponent.getPowers(camera.getEntity(), ModifyCameraSubmersionTypePower.class)) {
				if (p.doesModify(original)) {
					return p.getNewType();
				}
			}
		}
		return original;
	}

	@ModifyVariable(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 0), ordinal = 0)
	private static FogType modifyCameraSubmersionTypeFog(FogType original, Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float partialTicks) {
		if (camera.getEntity() instanceof LivingEntity) {
			for (ModifyCameraSubmersionTypePower p : PowerHolderComponent.getPowers(camera.getEntity(), ModifyCameraSubmersionTypePower.class)) {
				if (p.doesModify(original)) {
					return p.getNewType();
				}
			}
		}
		return original;
	}

    /*@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 1), ordinal = 0)
    private static double modifyD(double original, Camera camera) {
        if(camera.getFocusedEntity() instanceof LivingEntity) {
            if(PowerHolderComponent.getPowers(camera.getFocusedEntity(), PhasingPower.class).stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS)) {
                if(getInWallBlockState((PlayerEntity)camera.getFocusedEntity()) != null) {
                    return 0;
                }
            }
        }
        return original;
    }*/

    /*@ModifyVariable(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogStart(F)V"), ordinal = 0)
    private static float modifyS(float original, Camera camera) {
        List<LavaVisionPower> powers = IPowerContainer.getPowers(camera.getFocusedEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getS();
        }
        return original;
    }

    @ModifyVariable(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogStart(F)V"), ordinal = 1)
    private static float modifyV(float original, Camera camera) {
        List<LavaVisionPower> powers = IPowerContainer.getPowers(camera.getFocusedEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getV();
        }
        return original;
    }*/

	@ModifyConstant(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", constant = @Constant(floatValue = 0.25F, ordinal = 1), remap = false)
	private static float modifyLavaVisibilitySNoPotion(float original, Camera camera) {
		return LavaVisionPower.getS(camera.getEntity()).orElse(original);
	}

	@ModifyConstant(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", constant = @Constant(floatValue = 1.0F, ordinal = 0), remap = false)
	private static float modifyLavaVisibilityVNoPotion(float original, Camera camera) {
		return LavaVisionPower.getV(camera.getEntity()).orElse(original);
	}

	@ModifyConstant(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", constant = @Constant(floatValue = 0.0F, ordinal = 0), remap = false)
	private static float modifyLavaVisibilitySWithPotion(float original, Camera camera) {
		return LavaVisionPower.getS(camera.getEntity()).orElse(original);
	}

	@ModifyConstant(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", constant = @Constant(floatValue = 3.0F, ordinal = 0), remap = false)
	private static float modifyLavaVisibilityVWithPotion(float original, Camera camera) {
		return LavaVisionPower.getV(camera.getEntity()).orElse(original);
	}
}
