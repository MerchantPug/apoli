package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * @deprecated Unused in origins.
 */
@Deprecated
public class SimpleStatusEffectPower extends PowerFactory<ListConfiguration<MobEffectInstance>> {
	public SimpleStatusEffectPower(Codec<ListConfiguration<MobEffectInstance>> codec) {
		super(ListConfiguration.codec(SerializableDataTypes.STATUS_EFFECT_INSTANCE, "effect", "effects"));
		this.ticking();
	}

	@Override
	public int tickInterval(ConfiguredPower<ListConfiguration<MobEffectInstance>, ?> configuration, LivingEntity player) {
		return 10;
	}

	@Override
	protected void tick(ListConfiguration<MobEffectInstance> configuration, LivingEntity player) {
		configuration.getContent().forEach(sei -> player.addEffect(new MobEffectInstance(sei)));
	}
}
