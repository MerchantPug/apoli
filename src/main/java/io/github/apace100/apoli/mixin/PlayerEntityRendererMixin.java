package io.github.apace100.apoli.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModelColorPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {

    @Environment(EnvType.CLIENT)
    @Redirect(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 0))
    private void makeArmTranslucent(ModelPart modelPart, PoseStack matrices, VertexConsumer vertices, int light, int overlay, PoseStack matrices2, MultiBufferSource vertexConsumers, int light2, AbstractClientPlayer player) {
        List<ModelColorPower> modelColorPowers = PowerHolderComponent.KEY.get(player).getPowers(ModelColorPower.class);
        if (modelColorPowers.size() > 0) {
            float red = modelColorPowers.stream().map(ModelColorPower::getRed).reduce((a, b) -> a * b).get();
            float green = modelColorPowers.stream().map(ModelColorPower::getGreen).reduce((a, b) -> a * b).get();
            float blue = modelColorPowers.stream().map(ModelColorPower::getBlue).reduce((a, b) -> a * b).get();
            float alpha = modelColorPowers.stream().map(ModelColorPower::getAlpha).min(Float::compare).get();
            modelPart.render(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), light, overlay, red, green, blue, alpha);
            return;
        }
        modelPart.render(matrices, vertices, light, overlay);
    }

    @Environment(EnvType.CLIENT)
    @Redirect(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1))
    private void makeSleeveTranslucent(ModelPart modelPart, PoseStack matrices, VertexConsumer vertices, int light, int overlay, PoseStack matrices2, MultiBufferSource vertexConsumers, int light2, AbstractClientPlayer player) {
        List<ModelColorPower> modelColorPowers = PowerHolderComponent.KEY.get(player).getPowers(ModelColorPower.class);
        if (modelColorPowers.size() > 0) {
            float red = modelColorPowers.stream().map(ModelColorPower::getRed).reduce((a, b) -> a * b).get();
            float green = modelColorPowers.stream().map(ModelColorPower::getGreen).reduce((a, b) -> a * b).get();
            float blue = modelColorPowers.stream().map(ModelColorPower::getBlue).reduce((a, b) -> a * b).get();
            float alpha = modelColorPowers.stream().map(ModelColorPower::getAlpha).min(Float::compare).get();
            modelPart.render(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), light, overlay, red, green, blue, alpha);
            return;
        }
        modelPart.render(matrices, vertices, light, overlay);
    }
}
