package io.github.edwinmindcraft.apoli.common.registry.condition;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.condition.bientity.RelativeRotationCondition;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.bientity.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.entity.*;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.BIENTITY_CONDITIONS;

public class ApoliBiEntityConditions {
	public static final BiPredicate<ConfiguredBiEntityCondition<?, ?>, Pair<Entity, Entity>> PREDICATE = (config, pair) -> config.check(pair.getFirst(), pair.getSecond());

	private static <U extends BiEntityCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.BIENTITY_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedBiEntityCondition<ConstantConfiguration<Pair<Entity, Entity>>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedBiEntityCondition<ConditionStreamConfiguration<ConfiguredBiomeCondition<?, ?>, Pair<Entity, Entity>>>> AND = of("and");
	public static final RegistryObject<DelegatedBiEntityCondition<ConditionStreamConfiguration<ConfiguredBiomeCondition<?, ?>, Pair<Entity, Entity>>>> OR = of("or");

	public static final RegistryObject<DualBiEntityCondition> INVERT = BIENTITY_CONDITIONS.register("invert", DualBiEntityCondition::invert);
	public static final RegistryObject<DispatchBiEntityCondition> ACTOR_CONDITION = BIENTITY_CONDITIONS.register("actor_condition", DispatchBiEntityCondition::actor);
	public static final RegistryObject<DispatchBiEntityCondition> TARGET_CONDITION = BIENTITY_CONDITIONS.register("target_condition", DispatchBiEntityCondition::target);
	public static final RegistryObject<DispatchBiEntityCondition> EITHER = BIENTITY_CONDITIONS.register("either", DispatchBiEntityCondition::either);
	public static final RegistryObject<DispatchBiEntityCondition> BOTH = BIENTITY_CONDITIONS.register("both", DispatchBiEntityCondition::both);
	public static final RegistryObject<DualBiEntityCondition> UNDIRECTED = BIENTITY_CONDITIONS.register("undirected", DualBiEntityCondition::undirected);
	public static final RegistryObject<DoubleComparingBiEntityCondition> DISTANCE = BIENTITY_CONDITIONS.register("distance", () -> new DoubleComparingBiEntityCondition(Entity::distanceTo));
	public static final RegistryObject<LineOfSightCondition> CAN_SEE = BIENTITY_CONDITIONS.register("can_see", LineOfSightCondition::new);
	public static final RegistryObject<SimpleBiEntityCondition> OWNER = BIENTITY_CONDITIONS.register("owner", () -> new SimpleBiEntityCondition((actor, target) -> target instanceof TamableAnimal animal && Objects.equals(actor, animal.getOwner())));
	public static final RegistryObject<SimpleBiEntityCondition> RIDING = BIENTITY_CONDITIONS.register("riding", () -> new SimpleBiEntityCondition((actor, target) -> Objects.equals(actor.getVehicle(), target)));
	public static final RegistryObject<SimpleBiEntityCondition> RIDING_ROOT = BIENTITY_CONDITIONS.register("riding_root", () -> new SimpleBiEntityCondition((actor, target) -> Objects.equals(actor.getRootVehicle(), target)));
	public static final RegistryObject<SimpleBiEntityCondition> RIDING_RECURSIVE = BIENTITY_CONDITIONS.register("riding_recursive", () -> new SimpleBiEntityCondition(SimpleBiEntityCondition::ridingRecursive));
	public static final RegistryObject<SimpleBiEntityCondition> ATTACK_TARGET = BIENTITY_CONDITIONS.register("attack_target", () -> new SimpleBiEntityCondition((actor, target) -> actor instanceof Mob mob && Objects.equals(target, mob.getTarget()) || actor instanceof NeutralMob n && Objects.equals(target, n.getTarget())));
	public static final RegistryObject<SimpleBiEntityCondition> ATTACKER = BIENTITY_CONDITIONS.register("attacker", () -> new SimpleBiEntityCondition((actor, target) -> target instanceof LivingEntity living && Objects.equals(actor, living.getLastHurtByMob())));
	public static final RegistryObject<RelativeRotationCondition> RELATIVE_ROTATION = BIENTITY_CONDITIONS.register("relative_rotation", RelativeRotationCondition::new);

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(BIENTITY_CONDITIONS, DelegatedBiEntityCondition::new, ConfiguredBiEntityCondition.CODEC, PREDICATE);
	}
}
