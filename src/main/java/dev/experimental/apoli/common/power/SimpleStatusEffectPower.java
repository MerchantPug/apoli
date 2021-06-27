package dev.experimental.apoli.common.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.ListConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

/**
 * @deprecated Unused in origins.
 */
@Deprecated
public class SimpleStatusEffectPower extends PowerFactory<ListConfiguration<StatusEffectInstance>> {
	public SimpleStatusEffectPower(Codec<ListConfiguration<StatusEffectInstance>> codec) {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects"));
		this.ticking();
	}

	@Override
	public int tickInterval(ConfiguredPower<ListConfiguration<StatusEffectInstance>, ?> configuration, LivingEntity player) {
		return 10;
	}

	@Override
	protected void tick(ListConfiguration<StatusEffectInstance> configuration, LivingEntity player) {
		configuration.getContent().forEach(sei -> player.addStatusEffect(new StatusEffectInstance(sei)));
	}
}
