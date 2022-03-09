package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.INightVisionPower;
import io.github.edwinmindcraft.apoli.common.power.PhasingPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.PhasingConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	private final HashMap<BlockPos, BlockState> savedStates = new HashMap<>();
	@Shadow
	@Final
	private Camera mainCamera;
	@Shadow
	@Final
	private Minecraft minecraft;
	@Shadow
	private PostChain postEffect;
	@Shadow
	private boolean effectActive;
	@Unique
	private ResourceLocation currentlyLoadedShader;
	@Shadow
	@Final
	private ResourceManager resourceManager;

	@Shadow
	public abstract void loadEffect(ResourceLocation identifier);

	@Inject(at = @At("TAIL"), method = "checkEntityPostEffect")
	private void loadShaderFromPowerOnCameraEntity(Entity entity, CallbackInfo ci) {
		if (ApoliPowers.SHADER.isPresent()) {
			IPowerContainer.withPower(this.minecraft.getCameraEntity(), ApoliPowers.SHADER.get(), null, shaderPower -> {
				ResourceLocation shaderLoc = shaderPower.getConfiguration().value();
				if (this.resourceManager.hasResource(shaderLoc)) {
					this.loadEffect(shaderLoc);
					this.currentlyLoadedShader = shaderLoc;
				}
			});
		}
	}

	@Inject(at = @At("HEAD"), method = "render")
	private void loadShaderFromPower(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (ApoliPowers.SHADER.isPresent()) {
			IPowerContainer.withPower(this.minecraft.getCameraEntity(), ApoliPowers.SHADER.get(), null, shaderPower -> {
				ResourceLocation shaderLoc = shaderPower.getConfiguration().value();
				if (this.currentlyLoadedShader != shaderLoc) {
					this.loadEffect(shaderLoc);
					this.currentlyLoadedShader = shaderLoc;
				}
			});
			if (!IPowerContainer.hasPower(this.minecraft.getCameraEntity(), ApoliPowers.SHADER.get()) && this.currentlyLoadedShader != null) {
				if (this.postEffect != null) {
					this.postEffect.close();
					this.postEffect = null;
				}
				this.effectActive = false;
				this.currentlyLoadedShader = null;
			}
		}
	}

	@Inject(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getMainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"),
			method = "render"
	)
	private void fixHudWithShaderEnabled(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
		RenderSystem.enableTexture();
	}
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void renderOverlayPowers(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        boolean hudHidden = this.client.options.hudHidden;
        boolean thirdPerson = !client.options.getPerspective().isFirstPerson();
        PowerHolderComponent.withPower(client.getCameraEntity(), OverlayPower.class, p -> {
            if(p.getDrawPhase() != OverlayPower.DrawPhase.ABOVE_HUD) {
                return false;
            }
            if(hudHidden && p.doesHideWithHud()) {
                return false;
            }
            if(thirdPerson && !p.shouldBeVisibleInThirdPerson()) {
                return false;
            }
            return true;
        }, OverlayPower::render);
    }

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getFramebuffer()Lnet/minecraft/client/gl/Framebuffer;"),
        method = "render"
    )
    private void fixHudWithShaderEnabled(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
        RenderSystem.enableTexture();
    }

    @Inject(at = @At("HEAD"), method = "toggleShadersEnabled", cancellable = true)
    private void disableShaderToggle(CallbackInfo ci) {
        PowerHolderComponent.withPower(client.getCameraEntity(), ShaderPower.class, null, shaderPower -> {
            Identifier shaderLoc = shaderPower.getShaderLocation();
            if(!shaderPower.isToggleable() && currentlyLoadedShader == shaderLoc) {
                ci.cancel();
            }
        });
    }
/*
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setCameraEntity(Lnet/minecraft/entity/Entity;)V"))
    private void updateShaderPowers(CallbackInfo ci) {
        if(OriginComponent.hasPower(client.getCameraEntity(), ShaderPower.class)) {
            OriginComponent.withPower(client.getCameraEntity(), ShaderPower.class, null, shaderPower -> {
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

	@Redirect(method = "getNightVisionScale", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getDuration()I"))
	private static int fixNightVision(MobEffectInstance instance) {
		if (instance != null)
			return instance.getDuration();
		return 0;
	}

	@Inject(at = @At("RETURN"), method = "getNightVisionScale", cancellable = true)
	private static void updateNightVisionScale(LivingEntity living, float tickDelta, CallbackInfoReturnable<Float> cir) {
		if (!living.hasEffect(MobEffects.NIGHT_VISION)) //Should fix the flickering
			INightVisionPower.getNightVisionStrength(living).ifPresent(cir::setReturnValue);
	}
    // NightVisionPower
    @Inject(at = @At("HEAD"), method = "getNightVisionStrength", cancellable = true)
    private static void getNightVisionStrength(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> info) {
        if (livingEntity instanceof PlayerEntity && !livingEntity.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            List<NightVisionPower> nvs = PowerHolderComponent.KEY.get(livingEntity).getPowers(NightVisionPower.class);
            Optional<Float> strength = nvs.stream().filter(NightVisionPower::isActive).map(NightVisionPower::getStrength).max(Float::compareTo);
            strength.ifPresent(info::setReturnValue);
        }
    }

    @Redirect(method = "getFov", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;"))
    private CameraSubmersionType modifySubmersionType(Camera camera) {
        CameraSubmersionType original = camera.getSubmersionType();
        if(camera.getFocusedEntity() instanceof LivingEntity) {
            for(ModifyCameraSubmersionTypePower p : PowerHolderComponent.getPowers(camera.getFocusedEntity(), ModifyCameraSubmersionTypePower.class)) {
                if(p.doesModify(original)) {
                    return p.getNewType();
                }
            }
        }
        return original;
    }

    private HashMap<BlockPos, BlockState> savedStates = new HashMap<>();

	// PHASING: remove_blocks
	@Inject(at = @At(value = "HEAD"), method = "render")
	private void beforeRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
		if (!ApoliPowers.PHASING.isPresent())
			return;
		Optional<Float> renderMethod = PhasingPower.getRenderMethod(this.mainCamera.getEntity(), PhasingConfiguration.RenderType.REMOVE_BLOCKS);
		ClientLevel level = this.minecraft.level;
		if (level == null)
			return;
		if (renderMethod.isPresent()) {
			//float view = renderMethod.get();
			Set<BlockPos> eyePositions = this.getEyePos(0.25F, 0.05F, 0.25F);
			Set<BlockPos> noLongerEyePositions = new HashSet<>();
			for (BlockPos p : this.savedStates.keySet()) {
				if (!eyePositions.contains(p)) {
					noLongerEyePositions.add(p);
				}
			}
			for (BlockPos eyePosition : noLongerEyePositions) {
				BlockState state = this.savedStates.get(eyePosition);
				level.setBlockAndUpdate(eyePosition, state);
				this.savedStates.remove(eyePosition);
			}
			for (BlockPos p : eyePositions) {
				BlockState stateAtP = level.getBlockState(p);
				if (!this.savedStates.containsKey(p) && !level.isEmptyBlock(p) && !(stateAtP.getBlock() instanceof LiquidBlock)) {
					this.savedStates.put(p, stateAtP);
					level.setKnownState(p, Blocks.AIR.defaultBlockState());
				}
			}
		} else if (this.savedStates.size() > 0) {
			Set<BlockPos> noLongerEyePositions = new HashSet<>(this.savedStates.keySet());
			for (BlockPos eyePosition : noLongerEyePositions) {
				BlockState state = this.savedStates.get(eyePosition);
				level.setBlockAndUpdate(eyePosition, state);
				this.savedStates.remove(eyePosition);
			}
		}
	}

	// PHASING
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V"), method = "renderLevel")
	private void preventThirdPerson(Camera camera, BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
		if (ApoliPowers.PHASING.isPresent() && PhasingPower.hasRenderMethod(camera.getEntity(), PhasingConfiguration.RenderType.REMOVE_BLOCKS))
			camera.setup(area, focusedEntity, false, false, tickDelta);
		else
			camera.setup(area, focusedEntity, thirdPerson, inverseView, tickDelta);
	}

	private Set<BlockPos> getEyePos(float rangeX, float rangeY, float rangeZ) {
		Vec3 pos = this.mainCamera.getEntity().position().add(0, this.mainCamera.getEntity().getEyeHeight(this.mainCamera.getEntity().getPose()), 0);
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
