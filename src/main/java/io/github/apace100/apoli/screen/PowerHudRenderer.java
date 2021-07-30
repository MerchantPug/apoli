package io.github.apace100.apoli.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.HudRendered;
import io.github.apace100.apoli.util.HudRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PowerHudRenderer extends GuiComponent implements GameHudRender {

    @Override
    @Environment(EnvType.CLIENT)
    public void render(PoseStack matrices, float delta) {
        Minecraft client = Minecraft.getInstance();
        PowerHolderComponent component = PowerHolderComponent.KEY.get(client.player);
        // TODO: Reintroduce config for this
        int x = client.getWindow().getGuiScaledWidth() / 2 + 20;// + OriginsClient.config.xOffset;
        int y = client.getWindow().getGuiScaledHeight() - 47;// + OriginsClient.config.yOffset;
        Entity vehicle = client.player.getVehicle();
        if(vehicle instanceof LivingEntity) {
            y -= 8 * (int)(((LivingEntity)vehicle).getMaxHealth() / 20f);
        }
        if(client.player.isEyeInFluid(FluidTags.WATER) || client.player.getAirSupply() < client.player.getMaxAirSupply()) {
            y -= 8;
        }
        int barWidth = 71;
        int barHeight = 8;
        int iconSize = 8;
        List<HudRendered> hudPowers = component.getPowers().stream().filter(p -> p instanceof HudRendered).map(p -> (HudRendered)p).sorted(
            Comparator.comparing(hudRenderedA -> hudRenderedA.getRenderSettings().getSpriteLocation())
        ).collect(Collectors.toList());
        ResourceLocation lastLocation = null;
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        for (HudRendered hudPower : hudPowers) {
            HudRender render = hudPower.getRenderSettings();
            if(render.shouldRender(client.player) && hudPower.shouldRender()) {
                ResourceLocation currentLocation = render.getSpriteLocation();
                if(currentLocation != lastLocation) {
                    RenderSystem.setShaderTexture(0, currentLocation);
                    lastLocation = currentLocation;
                }
                blit(matrices, x, y, 0, 0, barWidth, 5);
                int v = 8 + render.getBarIndex() * 10;
                int w = (int)(hudPower.getFill() * barWidth);
                blit(matrices, x, y - 2, 0, v, w, barHeight);
                setBlitOffset(getBlitOffset() + 1);
                blit(matrices, x - iconSize - 2, y - 2, 73, v, iconSize, iconSize);
                setBlitOffset(getBlitOffset() - 1);
                y -= 8;
            }
        }
    }
}