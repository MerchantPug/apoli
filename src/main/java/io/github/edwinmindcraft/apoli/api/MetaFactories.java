package io.github.edwinmindcraft.apoli.api;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class MetaFactories {

	public static <F extends IForgeRegistryEntry<F>, C, V> void defineMetaConditions(DeferredRegister<F> registry, Function<Codec<? extends IDelegatedConditionConfiguration<V>>, ? extends F> func, Codec<C> conditionCodec, BiPredicate<C, V> predicate) {
		registry.register("constant", () -> func.apply(ConstantConfiguration.codec()));
		registry.register("and", () -> func.apply(ConditionStreamConfiguration.andCodec(conditionCodec, predicate)));
		registry.register("or", () -> func.apply(ConditionStreamConfiguration.orCodec(conditionCodec, predicate)));
	}

	/**
	 * Registers default actions for the given type.
	 * Actions are registered with the namespace of the registry.
	 *
	 * @param registry       The {@link IForgeRegistry} to register the actions for.
	 * @param func           The that creates a factory from a {@link IDelegatedActionConfiguration}
	 * @param actionCodec    The codec to serialize the action type.
	 * @param conditionCodec The codec to serialize the condition type.
	 * @param executor       The function used to execute an instance of the action type.
	 * @param predicate      The predicate used to test an instance of the condition type.
	 * @param <F>            The type of the action factory.
	 * @param <A>            The type of the configured action.
	 * @param <C>            The type of the configured condition.
	 * @param <V>            The intermediate type to reduce arguments to a single type.
	 */
	public static <F extends IForgeRegistryEntry<F>, A, C, V> void defineMetaActions(DeferredRegister<F> registry, Function<Codec<? extends IDelegatedActionConfiguration<V>>, ? extends F> func, Codec<A> actionCodec, Codec<C> conditionCodec, BiConsumer<A, V> executor, BiPredicate<C, V> predicate) {
		registry.register("and", () -> func.apply(StreamConfiguration.and(actionCodec, executor)));
		registry.register("chance", () -> func.apply(ChanceConfiguration.codec(actionCodec, executor)));
		registry.register("if_else", () -> func.apply(IfElseConfiguration.codec(conditionCodec, actionCodec, predicate, executor)));
		registry.register("if_else_list", () -> func.apply(StreamConfiguration.ifElseList(conditionCodec, actionCodec, predicate, executor)));
		registry.register("choice", () -> func.apply(ChoiceConfiguration.codec(actionCodec, executor)));
		registry.register("delay", () -> func.apply(DelayAction.codec(actionCodec, executor)));
		registry.register("nothing", () -> func.apply(NothingConfiguration.codec()));
	}
}
