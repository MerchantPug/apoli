package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.NightVisionPower;
import io.github.apace100.apoli.power.PhasingPower;
import io.github.apace100.apoli.power.ShaderPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    @Final
    private Minecraft client;

    @Shadow
    private ItemStack floatingItem;

    @Shadow
    protected abstract void method_31136(float f);

    @Shadow
    protected abstract void loadShader(ResourceLocation identifier);

    @Shadow
    private PostChain shader;
    @Shadow
    private boolean shadersEnabled;
    @Unique
    private ResourceLocation currentlyLoadedShader;

    @Inject(at = @At("TAIL"), method = "onCameraEntitySet")
    private void loadShaderFromPowerOnCameraEntity(Entity entity, CallbackInfo ci) {
        PowerHolderComponent.withPower(client.getCameraEntity(), ShaderPower.class, null, shaderPower -> {
            ResourceLocation shaderLoc = shaderPower.getShaderLocation();
            loadShader(shaderLoc);
            currentlyLoadedShader = shaderLoc;
        });
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void loadShaderFromPower(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        PowerHolderComponent.withPower(client.getCameraEntity(), ShaderPower.class, null, shaderPower -> {
            ResourceLocation shaderLoc = shaderPower.getShaderLocation();
            if(currentlyLoadedShader != shaderLoc) {
                loadShader(shaderLoc);
                currentlyLoadedShader = shaderLoc;
            }
        });
        if(!PowerHolderComponent.hasPower(client.getCameraEntity(), ShaderPower.class) && currentlyLoadedShader != null) {
            this.shader.close();
            this.shader = null;
            this.shadersEnabled = false;
            currentlyLoadedShader = null;
        }
    }

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getFramebuffer()Lnet/minecraft/client/gl/Framebuffer;"),
        method = "render"
    )
    private void fixHudWithShaderEnabled(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
        RenderSystem.enableTexture();
    }
/*
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setCameraEntity(Lnet/minecraft/entity/Entity;)V"))
    private void updateShaderPowers(CallbackInfo ci) {
        if(IPowerContainer.hasPower(client.getCameraEntity(), ShaderPower.class)) {
            IPowerContainer.withPower(client.getCameraEntity(), ShaderPower.class, null, shaderPower -> {
                Identifier shaderLoc = shaderPower.getShaderLocation();
                loadShader(shaderLoc);
                currentlyLoadedShader = shaderLoc;
            });
        } else {
            this.shader.close();
            this.shader = null;
            this.shadersEnabled = false;
            currentlyLoadedShader = null;
        }
    }*/

    // NightVisionPower
    @Inject(at = @At("HEAD"), method = "getNightVisionStrength", cancellable = true)
    private static void getNightVisionStrength(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> info) {
        if (livingEntity instanceof Player && !livingEntity.hasEffect(MobEffects.NIGHT_VISION)) {
            List<NightVisionPower> nvs = PowerHolderComponent.KEY.get(livingEntity).getPowers(NightVisionPower.class);
            Optional<Float> strength = nvs.stream().filter(NightVisionPower::isActive).map(NightVisionPower::getStrength).max(Float::compareTo);
            strength.ifPresent(info::setReturnValue);
        }
    }

    private HashMap<BlockPos, BlockState> savedStates = new HashMap<>();

    // PHASING: remove_blocks
    @Inject(at = @At(value = "HEAD"), method = "render")
    private void beforeRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        List<PhasingPower> phasings = PowerHolderComponent.getPowers(camera.getEntity(), PhasingPower.class);
        if (phasings.stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS)) {
            float view = phasings.stream().filter(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS).map(PhasingPower::getViewDistance).min(Float::compareTo).get();
            Set<BlockPos> eyePositions = getEyePos(0.25F, 0.05F, 0.25F);
            Set<BlockPos> noLongerEyePositions = new HashSet<>();
            for (BlockPos p : savedStates.keySet()) {
                if (!eyePositions.contains(p)) {
                    noLongerEyePositions.add(p);
                }
            }
            for (BlockPos eyePosition : noLongerEyePositions) {
                BlockState state = savedStates.get(eyePosition);
                client.level.setBlockAndUpdate(eyePosition, state);
                savedStates.remove(eyePosition);
            }
            for (BlockPos p : eyePositions) {
                BlockState stateAtP = client.level.getBlockState(p);
                if (!savedStates.containsKey(p) && !client.level.isEmptyBlock(p) && !(stateAtP.getBlock() instanceof LiquidBlock)) {
                    savedStates.put(p, stateAtP);
                    client.level.setKnownState(p, Blocks.AIR.defaultBlockState());
                }
            }
        } else if (savedStates.size() > 0) {
            Set<BlockPos> noLongerEyePositions = new HashSet<>(savedStates.keySet());
            for (BlockPos eyePosition : noLongerEyePositions) {
                BlockState state = savedStates.get(eyePosition);
                client.level.setBlockAndUpdate(eyePosition, state);
                savedStates.remove(eyePosition);
            }
        }
    }

    // PHASING
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"), method = "renderWorld")
    private void preventThirdPerson(Camera camera, BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        if (PowerHolderComponent.getPowers(camera.getEntity(), PhasingPower.class).stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS)) {
            camera.setup(area, focusedEntity, false, false, tickDelta);
        } else {
            camera.setup(area, focusedEntity, thirdPerson, inverseView, tickDelta);
        }
    }

    private Set<BlockPos> getEyePos(float rangeX, float rangeY, float rangeZ) {
        Vec3 pos = camera.getEntity().position().add(0, camera.getEntity().getEyeHeight(camera.getEntity().getPose()), 0);
        AABB cameraBox = new AABB(pos, pos);
        cameraBox = cameraBox.inflate(rangeX, rangeY, rangeZ);
        HashSet<BlockPos> set = new HashSet<>();
        BlockPos.betweenClosedStream(cameraBox).forEach(p -> set.add(p.immutable()));
        return set;
    }
    /* TODO: make this overlay independent of phasing power
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private void drawPhantomizedOverlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if(PowerHolderComponent.getPowers(this.client.player, PhasingPower.class).size() > 0 && !this.client.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            this.method_31136(OriginsClient.config.phantomizedOverlayStrength);
        }
    }*/
}
