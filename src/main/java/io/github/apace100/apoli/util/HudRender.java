package io.github.apace100.apoli.util;

import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.apace100.apoli.Apoli;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class HudRender {

	public static final HudRender DONT_RENDER = new HudRender(false, 0, Apoli.identifier("textures/gui/resource_bar.png"), null);

	private final boolean shouldRender;
	private final int barIndex;
	private final ResourceLocation spriteLocation;
	private final ConfiguredEntityCondition<?, ?> playerCondition;

	public HudRender(boolean shouldRender, int barIndex, ResourceLocation spriteLocation, ConfiguredEntityCondition<?, ?> condition) {
		this.shouldRender = shouldRender;
		this.barIndex = barIndex;
		this.spriteLocation = spriteLocation;
		this.playerCondition = condition;
	}

	public ResourceLocation getSpriteLocation() {
		return this.spriteLocation;
	}

	public int getBarIndex() {
		return this.barIndex;
	}

	public boolean shouldRender() {
		return this.shouldRender;
	}

	public boolean shouldRender(Player player) {
		return this.shouldRender && (this.playerCondition == null || this.playerCondition.check(player));
	}

	public ConfiguredEntityCondition<?, ?> getCondition() {
		return this.playerCondition;
	}
}
