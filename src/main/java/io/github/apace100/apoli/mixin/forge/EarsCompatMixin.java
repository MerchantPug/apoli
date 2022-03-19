package io.github.apace100.apoli.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unascribed.ears.common.render.IndirectEarsRenderDelegate;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(targets = "com.unascribed.ears.EarsLayerRenderer$1")
public abstract class EarsCompatMixin extends IndirectEarsRenderDelegate<PoseStack, MultiBufferSource, VertexConsumer, AbstractClientPlayer, ModelPart> {

	@Redirect(method = "addVertex", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), remap = false)
	public VertexConsumer inject(VertexConsumer consumer, float r, float g, float b, float alpha) {
		ColorConfiguration config = new ColorConfiguration(r, g, b, alpha);
		Optional<ColorConfiguration> opt = ColorConfiguration.forPower(this.peer, ApoliPowers.MODEL_COLOR.get());
		if (opt.isPresent()) {
			config = config.merge(opt.get());
		}
		return consumer.color(config.red(), config.green(), config.blue(), config.alpha());
	}
}
