package io.github.apace100.apoli.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public record HudRender(boolean shouldRender, int barIndex, ResourceLocation spriteLocation,
						ConfiguredEntityCondition<?, ?> condition) {

	public static final Codec<HudRender> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(Codec.BOOL, "should_render", true).forGetter(HudRender::shouldRender),
			CalioCodecHelper.optionalField(Codec.INT, "bar_index", 0).forGetter(HudRender::barIndex),
			CalioCodecHelper.optionalField(ResourceLocation.CODEC, "sprite_location", Apoli.identifier("textures/gui/resource_bar.png")).forGetter(HudRender::spriteLocation),
			CalioCodecHelper.optionalField(ConfiguredEntityCondition.CODEC, "condition").forGetter(x -> Optional.ofNullable(x.condition()))
	).apply(instance, (t1, t2, t3, t4) -> new HudRender(t1, t2, t3, t4.orElse(null))));

	public static final HudRender DONT_RENDER = new HudRender(false, 0, Apoli.identifier("textures/gui/resource_bar.png"), null);

	public ResourceLocation getSpriteLocation() {
		return this.spriteLocation();
	}

	public int getBarIndex() {
		return this.barIndex();
	}

	public boolean shouldRender(Player player) {
		return this.shouldRender() && (this.condition() == null || this.condition().check(player));
	}

	public ConfiguredEntityCondition<?, ?> getCondition() {
		return this.condition();
	}
}
