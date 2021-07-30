package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.LavaVisionPower;
import io.github.apace100.apoli.power.NightVisionPower;
import io.github.apace100.apoli.power.PhasingPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

@Mixin(FogRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class BackgroundRendererMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 1), method = "render")
    private static boolean hasStatusEffectProxy(LivingEntity player, MobEffect effect) {
        if(player instanceof Player && effect == MobEffects.NIGHT_VISION && !player.hasEffect(MobEffects.NIGHT_VISION)) {
            return PowerHolderComponent.KEY.get(player).getPowers(NightVisionPower.class).stream().anyMatch(NightVisionPower::isActive);
        }
        return player.hasEffect(effect);
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 1), ordinal = 0)
    private static double modifyD(double original, Camera camera) {
        if(camera.getEntity() instanceof LivingEntity) {
            if(PowerHolderComponent.getPowers(camera.getEntity(), PhasingPower.class).stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS)) {
                if(getInWallBlockState((Player)camera.getEntity()) != null) {
                    return 0;
                }
            }
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

    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
    private static void redirectFogStart(float start, Camera camera, FogRenderer.FogMode fogType) {
        if(camera.getEntity() instanceof LivingEntity) {
            List<PhasingPower> phasings = PowerHolderComponent.getPowers(camera.getEntity(), PhasingPower.class);
            if(phasings.stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS)) {
                if(getInWallBlockState((LivingEntity)camera.getEntity()) != null) {
                    float view = phasings.stream().filter(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS).map(PhasingPower::getViewDistance).min(Float::compareTo).get();
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
        }
        RenderSystem.setShaderFogStart(start);
    }

    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"))
    private static void redirectFogEnd(float end, Camera camera, FogRenderer.FogMode fogType) {
        if(camera.getEntity() instanceof LivingEntity) {
            List<PhasingPower> phasings = PowerHolderComponent.getPowers(camera.getEntity(), PhasingPower.class);
            if(phasings.stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS)) {
                if(getInWallBlockState((Player)camera.getEntity()) != null) {
                    float view = phasings.stream().filter(pp -> pp.getRenderType() == PhasingPower.RenderType.BLINDNESS).map(PhasingPower::getViewDistance).min(Float::compareTo).get();
                    float v;
                    if (fogType == FogRenderer.FogMode.FOG_SKY) {
                        v = Math.min(view * 0.8F, end);
                    } else {
                        v = Math.min(view, end);
                    }
                    RenderSystem.setShaderFogEnd(v);
                    return;
                }
            }
        }
        RenderSystem.setShaderFogEnd(end);
    }

    private static BlockState getInWallBlockState(LivingEntity playerEntity) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 8; ++i) {
            double d = playerEntity.getX() + (double)(((float)((i >> 0) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
            double e = playerEntity.getEyeY() + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = playerEntity.getZ() + (double)(((float)((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
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

 */ @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 0.25F, ordinal = 1))
    private static float modifyLavaVisibilitySNoPotion(float original, Camera camera) {
        List<LavaVisionPower> powers = PowerHolderComponent.getPowers(camera.getEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getS();
        }
        return original;
    }

    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private static float modifyLavaVisibilityVNoPotion(float original, Camera camera) {
        List<LavaVisionPower> powers = PowerHolderComponent.getPowers(camera.getEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getV();
        }
        return original;
    }

    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 0.0F, ordinal = 0))
    private static float modifyLavaVisibilitySWithPotion(float original, Camera camera) {
        List<LavaVisionPower> powers = PowerHolderComponent.getPowers(camera.getEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getS();
        }
        return original;
    }

    @ModifyConstant(method = "applyFog", constant = @Constant(floatValue = 3.0F, ordinal = 0))
    private static float modifyLavaVisibilityVWithPotion(float original, Camera camera) {
        List<LavaVisionPower> powers = PowerHolderComponent.getPowers(camera.getEntity(), LavaVisionPower.class);
        if(powers.size() > 0) {
            return powers.get(0).getV();
        }
        return original;
    }
}
