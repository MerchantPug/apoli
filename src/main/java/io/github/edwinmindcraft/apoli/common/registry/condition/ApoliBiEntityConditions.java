package io.github.edwinmindcraft.apoli.common.registry.condition;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiomeCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.bientity.DelegatedBiEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.RegistryObject;

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

	public static void bootstrap() {
		MetaFactories.defineMetaConditions(BIENTITY_CONDITIONS, DelegatedBiEntityCondition::new, ConfiguredBiEntityCondition.CODEC, PREDICATE);
	}
}
