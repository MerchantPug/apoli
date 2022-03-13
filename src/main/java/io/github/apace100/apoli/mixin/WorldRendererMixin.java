package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.github.apace100.apoli.ApoliClient;
import io.github.edwinmindcraft.apoli.common.power.EntityGlowPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Iterator;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {

	@Unique
	private Entity renderEntity;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	public abstract void allChanged();

	@Inject(method = "renderLevel", at = @At("HEAD"))
	private void updateChunksIfRenderChanged(PoseStack outlinebuffersource, float i, long j, boolean k, Camera l, GameRenderer i1, LightTexture multibuffersource, Matrix4f matrix4f, CallbackInfo ci) {
		if (ApoliClient.shouldReloadWorldRenderer) {
			this.allChanged();
			ApoliClient.shouldReloadWorldRenderer = false;
		}
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void getEntity(PoseStack outlinebuffersource, float i, long j, boolean k, Camera l, GameRenderer i1, LightTexture multibuffersource, Matrix4f projection, CallbackInfo ci,
						   ProfilerFiller profilerfiller, boolean flag, Vec3 vec3, double d0, double d1, double d2, Matrix4f matrix4f, boolean flag1, Frustum frustum, float f,
						   boolean flag2, boolean flag3, MultiBufferSource.BufferSource immediate, Iterator var26, Entity entity) {
		this.renderEntity = entity;
	}

	//I Still can't get @ModifyArgs to not crash my game.
	@Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"))
	private int setColors(Entity instance) {
		Optional<ColorConfiguration> glowColor = EntityGlowPower.getGlowColor(this.minecraft.getCameraEntity(), this.renderEntity);
		return glowColor.map(ColorConfiguration::asRGB).orElseGet(instance::getTeamColor);
	}
}
