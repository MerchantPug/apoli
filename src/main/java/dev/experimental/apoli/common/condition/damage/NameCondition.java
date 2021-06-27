package dev.experimental.apoli.common.condition.damage;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import net.minecraft.entity.damage.DamageSource;

public class NameCondition extends DamageCondition<FieldConfiguration<String>> {

	public NameCondition() {
		super(FieldConfiguration.codec(Codec.STRING, "name"));
	}

	@Override
	public boolean check(FieldConfiguration<String> configuration, DamageSource source, float amount) {
		return configuration.value().equals(source.name);
	}
}
