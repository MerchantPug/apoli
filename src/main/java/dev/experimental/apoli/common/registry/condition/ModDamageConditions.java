package dev.experimental.apoli.common.registry.condition;

import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredDamageCondition;
import dev.experimental.apoli.common.condition.damage.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiPredicate;

import static dev.experimental.apoli.common.registry.ApoliRegisters.DAMAGE_CONDITIONS;

public class ModDamageConditions {
	public static final BiPredicate<ConfiguredDamageCondition<?, ?>, Pair<DamageSource, Float>> PREDICATE = (config, pair) -> config.check(pair.getLeft(), pair.getRight());

	public static final RegistryObject<AmountCondition> AMOUNT = DAMAGE_CONDITIONS.register("amount", AmountCondition::new);
	public static final RegistryObject<NameCondition> NAME = DAMAGE_CONDITIONS.register("name", NameCondition::new);
	public static final RegistryObject<FireDamageCondition> FIRE = DAMAGE_CONDITIONS.register("fire", FireDamageCondition::new);
	public static final RegistryObject<ProjectileCondition> PROJECTILE = DAMAGE_CONDITIONS.register("projectile", ProjectileCondition::new);
	public static final RegistryObject<AttackerCondition> ATTACKER = DAMAGE_CONDITIONS.register("attacker", AttackerCondition::new);

	public static void register() {
		MetaFactories.defineMetaConditions(DAMAGE_CONDITIONS, DelegatedDamageCondition::new, ConfiguredDamageCondition.CODEC, PREDICATE);
	}
}
