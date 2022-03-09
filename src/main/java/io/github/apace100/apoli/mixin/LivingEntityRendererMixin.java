package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> {

	protected LivingEntityRendererMixin(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
	private void letPlayersShakeTheirBodies(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (IPowerContainer.hasPower(entity, ApoliPowers.SHAKING.get()))
			cir.setReturnValue(true);
	}

	@ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", shift = At.Shift.BEFORE))
	private RenderType changeRenderLayerWhenTranslucent(RenderType original, LivingEntity entity) {
		if (entity instanceof Player) {
			return ColorConfiguration.forPower(entity, ApoliPowers.MODEL_COLOR.get()).filter(x -> x.alpha() < 1F)
					.map(x -> RenderType.itemEntityTranslucentCull(this.getTextureLocation(entity))).orElse(original);
		}
		return original;
	}
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void preventPumpkinRendering(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        List<InvisibilityPower> invisibilityPowers = PowerHolderComponent.getPowers(livingEntity, InvisibilityPower.class);
        if(invisibilityPowers.size() > 0 && invisibilityPowers.stream().noneMatch(InvisibilityPower::shouldRenderArmor)) {
            info.cancel();
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;", shift = At.Shift.BEFORE))
    private RenderLayer changeRenderLayerWhenTranslucent(RenderLayer original, LivingEntity entity) {
        if(entity != null) {
            if(PowerHolderComponent.getPowers(entity, ModelColorPower.class).stream().anyMatch(ModelColorPower::isTranslucent)) {
                return RenderLayer.getItemEntityTranslucentCull(getTexture(entity));
            }
        }
        return original;
    }

	@Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
	private void renderColorChangedModel(EntityModel<LivingEntity> model, PoseStack postStack, VertexConsumer vertexConsumer, int p1, int overlay, float red, float green, float blue, float alpha, LivingEntity living) {
		Optional<ColorConfiguration> opt = ColorConfiguration.forPower(living, ApoliPowers.MODEL_COLOR.get());
		if (opt.isPresent()) {
			ColorConfiguration color = opt.get();
			red *= color.red();
			green *= color.green();
			blue *= color.blue();
			alpha *= color.alpha();
		}
		model.renderToBuffer(postStack, vertexConsumer, p1, overlay, red, green, blue, alpha);
	}
	/*
	FIXME: Something is wrong with mixin's ClassGenerators, ModifyArgs cannot generate a class.
	 When this is fixed, I'll use this again, but for now, I'm using a redirect.
	@OnlyIn(Dist.CLIENT)
	@ModifyArgs(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", ordinal = 0)
	)
	private void renderColorChangedModel(Args args, LivingEntity living, float f1, float f2, PoseStack ps, MultiBufferSource source, int i) {
		ColorConfiguration.forPower(living, ModPowers.MODEL_COLOR.get()).ifPresent(color -> {
			//Mixin is being weird.
			//Basically: if there is a redirect, args[0] is a Model, otherwise args[0] is the PoseStack
			int red = args.size() - 4;
			int green = args.size() - 3;
			int blue = args.size() - 2;
			int alpha = args.size() - 1;
			args.set(red, args.<Float>get(red) * color.red());
			args.set(green, args.<Float>get(green) * color.green());
			args.set(blue, args.<Float>get(blue) * color.blue());
			args.set(alpha, args.<Float>get(alpha) * color.alpha());
		});
	}*/
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V"))
    private void preventFeatureRendering(FeatureRenderer featureRenderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        Class cls = featureRenderer.getClass();
        if(!PowerHolderComponent.getPowers(entity, PreventFeatureRenderPower.class).stream().anyMatch(p -> p.doesApply(cls))) {
            featureRenderer.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    @Environment(EnvType.CLIENT)
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void renderColorChangedModel(Args args, LivingEntity livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        List<ModelColorPower> modelColorPowers = PowerHolderComponent.getPowers(livingEntity, ModelColorPower.class);
        if (modelColorPowers.size() > 0) {
            float r = modelColorPowers.stream().map(ModelColorPower::getRed).reduce((a, b) -> a * b).get();
            float g = modelColorPowers.stream().map(ModelColorPower::getGreen).reduce((a, b) -> a * b).get();
            float b = modelColorPowers.stream().map(ModelColorPower::getBlue).reduce((a, c) -> a * c).get();
            float a = modelColorPowers.stream().map(ModelColorPower::getAlpha).min(Float::compare).get();
            args.set(4, (float) args.get(4) * r);
            args.set(5, (float) args.get(5) * g);
            args.set(6, (float) args.get(6) * b);
            args.set(7, (float) args.get(7) * a);
        }
    }
}
