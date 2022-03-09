package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
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
	public int tickInterval(ConfiguredPower<ListConfiguration<MobEffectInstance>, ?> configuration, Entity player) {
		return 10;
	}

	@Override
	protected void tick(ListConfiguration<MobEffectInstance> configuration, Entity entity) {
		if (!(entity instanceof LivingEntity living))
			return;
		configuration.getContent().forEach(sei -> living.addEffect(new MobEffectInstance(sei)));
	}
}
