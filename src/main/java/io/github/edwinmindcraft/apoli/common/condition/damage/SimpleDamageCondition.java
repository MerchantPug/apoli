package io.github.edwinmindcraft.apoli.common.condition.damage;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.DamageCondition;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SimpleDamageCondition extends DamageCondition<NoConfiguration> {
	private final BiPredicate<DamageSource, Float> predicate;

	public SimpleDamageCondition(Predicate<DamageSource> predicate) {
		this((source, amount) -> predicate.test(source));
	}

	public SimpleDamageCondition(BiPredicate<DamageSource, Float> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, DamageSource source, float amount) {
		return this.predicate.test(source, amount);
	}
}
