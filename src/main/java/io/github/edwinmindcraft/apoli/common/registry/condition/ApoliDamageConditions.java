package io.github.edwinmindcraft.apoli.common.registry.condition;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.DamageCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.damage.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.core.HolderSet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.DAMAGE_CONDITIONS;

public class ApoliDamageConditions {
	public static final BiPredicate<ConfiguredDamageCondition<?, ?>, Pair<DamageSource, Float>> PREDICATE = (config, pair) -> config.check(pair.getLeft(), pair.getRight());

	private static <U extends DamageCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.create(Apoli.identifier(name), ApoliRegistries.DAMAGE_CONDITION_KEY.location(), Apoli.MODID);
	}

	public static final RegistryObject<DelegatedDamageCondition<ConstantConfiguration<Pair<DamageSource, Float>>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedDamageCondition<ConditionStreamConfiguration<ConfiguredDamageCondition<?, ?>, Pair<DamageSource, Float>>>> AND = of("and");
	public static final RegistryObject<DelegatedDamageCondition<ConditionStreamConfiguration<ConfiguredDamageCondition<?, ?>, Pair<DamageSource, Float>>>> OR = of("or");

	public static final RegistryObject<AmountCondition> AMOUNT = DAMAGE_CONDITIONS.register("amount", AmountCondition::new);
	public static final RegistryObject<NameCondition> NAME = DAMAGE_CONDITIONS.register("name", NameCondition::new);
	public static final RegistryObject<FireDamageCondition> FIRE = DAMAGE_CONDITIONS.register("fire", FireDamageCondition::new);
	public static final RegistryObject<ProjectileCondition> PROJECTILE = DAMAGE_CONDITIONS.register("projectile", ProjectileCondition::new);
	public static final RegistryObject<AttackerCondition> ATTACKER = DAMAGE_CONDITIONS.register("attacker", AttackerCondition::new);

	public static ConfiguredDamageCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	@SafeVarargs
	public static ConfiguredDamageCondition<?, ?> and(HolderSet<ConfiguredDamageCondition<?, ?>>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	@SafeVarargs
	public static ConfiguredDamageCondition<?, ?> or(HolderSet<ConfiguredDamageCondition<?, ?>>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(DAMAGE_CONDITIONS, DelegatedDamageCondition::new, ConfiguredDamageCondition.CODEC_SET, PREDICATE);
	}
}
