package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.edwinmindcraft.apoli.api.power.INightVisionPower;
import io.github.edwinmindcraft.apoli.common.power.LavaVisionPower;
import io.github.edwinmindcraft.apoli.common.power.PhasingPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.PhasingConfiguration;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

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

	@ModifyVariable(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 1), ordinal = 0)
	private static double modifyD(double original, Camera camera) {
		if (camera.getEntity() instanceof LivingEntity) {
			if (PhasingPower.hasRenderMethod(camera.getEntity(), PhasingConfiguration.RenderType.BLINDNESS) && getInWallBlockState((Player) camera.getEntity()) != null)
				return 0;
		}
		return original;
	}

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

	@Redirect(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
	private static void redirectFogStart(float start, Camera camera, FogRenderer.FogMode fogType) {
		if (camera.getEntity() instanceof LivingEntity) {
			Optional<Float> renderMethod = PhasingPower.getRenderMethod(camera.getEntity(), PhasingConfiguration.RenderType.BLINDNESS);
			if (renderMethod.isPresent() && getInWallBlockState((Player) camera.getEntity()) != null) {
				float view = renderMethod.get();
				float s;
				if (fogType == FogRenderer.FogMode.FOG_SKY) {
					s = Math.min(0F, start);
				} else {
					s = Math.min(view * 0.25F, start);
				}
				RenderSystem.setShaderFogStart(s);
				return;
			}
		}
		RenderSystem.setShaderFogStart(start);
	}

	@Redirect(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"))
	private static void redirectFogEnd(float end, Camera camera, FogRenderer.FogMode fogType) {
		if (camera.getEntity() instanceof LivingEntity) {
			Optional<Float> renderMethod = PhasingPower.getRenderMethod(camera.getEntity(), PhasingConfiguration.RenderType.BLINDNESS);
			if (renderMethod.isPresent() && getInWallBlockState((Player) camera.getEntity()) != null) {
				float view = renderMethod.get();
				float v;
				if (fogType == FogRenderer.FogMode.FOG_SKY)
					v = Math.min(view * 0.8F, end);
				else
					v = Math.min(view, end);
				RenderSystem.setShaderFogEnd(v);
				return;
			}
		}
		RenderSystem.setShaderFogEnd(end);
	}

	private static BlockState getInWallBlockState(LivingEntity playerEntity) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int i = 0; i < 8; ++i) {
			double d = playerEntity.getX() + (double) (((float) (i % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			double e = playerEntity.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
			double f = playerEntity.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			mutable.set(d, e, f);
			BlockState blockState = playerEntity.level.getBlockState(mutable);
			if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(playerEntity.level, mutable)) {
				return blockState;
			}
		}

		return null;
	}

	/*
		@Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 0))
		private static boolean allowUnderlavaVision(LivingEntity livingEntity, StatusEffect effect) {
			//if(PowerTypes.LAVA_SWIMMING.isActive(livingEntity)) {
			//    return true;
			//}
			return livingEntity.hasStatusEffect(effect);
		}

		@ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 3.0F, ordinal = 0))
		private static float modifyLavaVisibility(float original, Camera camera) {
			//if(PowerTypes.LAVA_SWIMMING.isActive(camera.getFocusedEntity())) {
			//    return original * 5F;
			//}
			return original;
		}

	 */
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
