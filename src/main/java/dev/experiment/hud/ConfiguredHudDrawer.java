package dev.experiment.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.apace100.apoli.screen.GameHudRender;
import io.github.apace100.apoli.util.ApoliConfigs;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

@OnlyIn(Dist.CLIENT)
public enum ConfiguredHudDrawer implements GameHudRender {
	INSTANCE;
	private static final int BAR_WIDTH = 71;

	@Override
	public void render(PoseStack matrixStack, float tickDelta) {
		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;
		LazyOptional<IPowerContainer> containerOptional = IPowerContainer.get(player);
		if (!containerOptional.isPresent() || player == null)
			return;
		IPowerContainer container = containerOptional.orElseThrow(RuntimeException::new);

		int x = client.getWindow().getGuiScaledWidth() / 2 + 20 + ApoliConfigs.CLIENT.resourcesAndCooldowns.hudOffsetX.get();
		int y = client.getWindow().getGuiScaledHeight() - 47 + ApoliConfigs.CLIENT.resourcesAndCooldowns.hudOffsetY.get();
		if (player.getVehicle() instanceof LivingEntity vehicle)
			y -= 8 * (int) (vehicle.getMaxHealth() / 20f);
		if (player.isEyeInFluid(FluidTags.WATER) || player.getAirSupply() < player.getMaxAirSupply())
			y -= 8;

		for (ConfiguredPower<?, ?> power : container.getPowers()) {
			ConfiguredHudRenderer<?, ?> renderer = null; //FIXME Actually implement this.
			DrawType drawType = renderer.shouldDraw(player);
			if (!power.shouldRender(player).map(drawType).orElse(false)) continue;
			int height = renderer.height(player);
			float fill = power.getFill(player).orElse(0.0F);
			renderer.drawBar(player, matrixStack, x - 2, y - height + 6, BAR_WIDTH, fill);
			renderer.drawIcon(player, matrixStack, x - height - 2, y - height + 6, fill);
			y -= height;
		}
	}
}
