package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IHudRenderedVariableIntPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IVariableIntPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ResourceConfiguration(HudRender hudRender, int initialValue, int min, int max,
									@Nullable ConfiguredEntityAction<?, ?> minAction,
									@Nullable ConfiguredEntityAction<?, ?> maxAction) implements IHudRenderedVariableIntPowerConfiguration {
	public static final Codec<ResourceConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(IHudRenderedVariableIntPowerConfiguration::hudRender),
			CalioCodecHelper.optionalField(Codec.INT, "start_value").forGetter(x -> x.min() == x.initialValue() ? Optional.empty() : Optional.of(x.initialValue())),
			Codec.INT.fieldOf("min").forGetter(IVariableIntPowerConfiguration::min),
			Codec.INT.fieldOf("max").forGetter(IVariableIntPowerConfiguration::max),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "min_action").forGetter(x -> Optional.ofNullable(x.minAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "max_action").forGetter(x -> Optional.ofNullable(x.maxAction()))
	).apply(instance, (t1, t2, t3, t4, t5, t6) -> new ResourceConfiguration(t1, t2.orElse(t3), t3, t4, t5.orElse(null), t6.orElse(null))));
}
