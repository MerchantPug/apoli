package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.ICooldownPowerConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record LaunchConfiguration(int duration, float speed, @Nullable SoundEvent sound, HudRender hudRender,
								  IActivePower.Key key) implements IActiveCooldownPowerConfiguration {
	public static final Codec<LaunchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ICooldownPowerConfiguration::duration),
			Codec.FLOAT.fieldOf("speed").forGetter(LaunchConfiguration::speed),
			CalioCodecHelper.optionalField(SerializableDataTypes.SOUND_EVENT, "sound").forGetter(x -> Optional.ofNullable(x.sound())),
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(ICooldownPowerConfiguration::hudRender),
			CalioCodecHelper.optionalField(IActivePower.Key.CODEC, "key", IActivePower.Key.PRIMARY).forGetter(IActiveCooldownPowerConfiguration::key)
	).apply(instance, (t1, t2, t3, t4, t5) -> new LaunchConfiguration(t1, t2, t3.orElse(null), t4, t5)));
}
