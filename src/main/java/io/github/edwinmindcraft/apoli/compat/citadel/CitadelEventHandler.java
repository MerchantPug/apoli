package io.github.edwinmindcraft.apoli.compat.citadel;

import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import io.github.edwinmindcraft.apoli.common.power.EntityGlowPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitadelEventHandler {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void entityOutline(EventGetOutlineColor event) {
		Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
		Entity entityIn = event.getEntityIn();
		if (cameraEntity != null) {
			EntityGlowPower.getGlowColor(cameraEntity, entityIn).map(ColorConfiguration::asRGB).ifPresent(color -> {
				event.setColor(color);
				event.setResult(Event.Result.ALLOW);
			});
		}
	}
}
