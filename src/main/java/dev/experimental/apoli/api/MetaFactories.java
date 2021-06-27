package dev.experimental.apoli.api;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.Registrar;
import dev.experimental.apoli.common.action.meta.*;
import dev.experimental.apoli.common.condition.meta.ConditionStreamConfiguration;
import dev.experimental.apoli.common.condition.meta.ConstantConfiguration;
import dev.experimental.apoli.common.condition.meta.IDelegatedConditionConfiguration;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class MetaFactories {

	public static <F, C, V> void defineMetaConditions(Registrar<F> registry, Function<Codec<? extends IDelegatedConditionConfiguration<V>>, ? extends F> func, Codec<C> conditionCodec, BiPredicate<C, V> predicate) {
		String namespace = registry.key().getValue().getNamespace();
		registry.register(new Identifier(namespace, "constant"), () -> func.apply(ConstantConfiguration.codec()));
		registry.register(new Identifier(namespace, "and"), () -> func.apply(ConditionStreamConfiguration.andCodec(conditionCodec, predicate)));
		registry.register(new Identifier(namespace, "or"), () -> func.apply(ConditionStreamConfiguration.orCodec(conditionCodec, predicate)));
	}

	/**
	 * Registers default actions for the given type.
	 * Actions are registered with the namespace of the registry.
	 *
	 * @param registry       The {@link Registrar} to register the actions for.
	 * @param func           The that creates a factory from a {@link IDelegatedActionConfiguration}
	 * @param actionCodec    The codec to serialize the action type.
	 * @param conditionCodec The codec to serialize the condition type.
	 * @param executor       The function used to execute the an instance of the action type.
	 * @param predicate      The predicate used to test an instance of the condition type.
	 * @param <F>            The type of the action factory.
	 * @param <A>            The type of the configured action.
	 * @param <C>            The type of the configured condition.
	 * @param <V>            The intermediate type to reduce arguments to a single type.
	 */
	public static <F, A, C, V> void defineMetaActions(Registrar<F> registry, Function<Codec<? extends IDelegatedActionConfiguration<V>>, ? extends F> func, Codec<A> actionCodec, Codec<C> conditionCodec, BiConsumer<A, V> executor, BiPredicate<C, V> predicate) {
		String namespace = registry.key().getValue().getNamespace();
		registry.register(new Identifier(namespace, "and"), () -> func.apply(StreamConfiguration.and(actionCodec, executor)));
		registry.register(new Identifier(namespace, "chance"), () -> func.apply(ChanceConfiguration.codec(actionCodec, executor)));
		registry.register(new Identifier(namespace, "if_else"), () -> func.apply(IfElseConfiguration.codec(conditionCodec, actionCodec, predicate, executor)));
		registry.register(new Identifier(namespace, "if_else_list"), () -> func.apply(StreamConfiguration.ifElseList(conditionCodec, actionCodec, predicate, executor)));
		registry.register(new Identifier(namespace, "choice"), () -> func.apply(ChoiceConfiguration.codec(actionCodec, executor)));
		registry.register(new Identifier(namespace, "delay"), () -> func.apply(DelayAction.codec(actionCodec, executor)));
	}
}
