package dev.experimental.apoli.common.registry.condition;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.damage.*;
import io.github.apace100.apoli.Apoli;
import net.minecraft.entity.damage.DamageSource;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ModDamageConditions {
	public static final BiPredicate<ConfiguredDamageCondition<?, ?>, Pair<DamageSource, Float>> PREDICATE = (config, pair) -> config.check(pair.getLeft(), pair.getRight());

	public static final RegistrySupplier<AmountCondition> AMOUNT = register("amount", AmountCondition::new);
	public static final RegistrySupplier<NameCondition> NAME = register("name", NameCondition::new);
	public static final RegistrySupplier<FireDamageCondition> FIRE = register("fire", FireDamageCondition::new);
	public static final RegistrySupplier<ProjectileCondition> PROJECTILE = register("projectile", ProjectileCondition::new);
	public static final RegistrySupplier<AttackerCondition> ATTACKER = register("attacker", AttackerCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.DAMAGE_CONDITION, DelegatedDamageCondition::new, ConfiguredDamageCondition.CODEC, PREDICATE);
	}

	private static <T extends DamageCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.DAMAGE_CONDITION.register(Apoli.identifier(name), factory);
	}
}
