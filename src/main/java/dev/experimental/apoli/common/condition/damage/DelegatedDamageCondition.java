package dev.experimental.apoli.common.condition.damage;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.entity.damage.DamageSource;
import org.apache.commons.lang3.tuple.Pair;

public class DelegatedDamageCondition<T extends IDelegatedConditionConfiguration<Pair<DamageSource, Float>>> extends DamageCondition<T> {
	public DelegatedDamageCondition(Codec<T> codec) {
		super(codec);
	}

	@Override
	protected boolean check(T configuration, DamageSource source, float amount) {
		return configuration.check(Pair.of(source, amount));
	}
}
