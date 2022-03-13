package io.github.edwinmindcraft.apoli.common.condition.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.world.entity.Entity;

import java.util.function.BinaryOperator;

public class DispatchBiEntityCondition extends BiEntityCondition<FieldConfiguration<ConfiguredEntityCondition<?, ?>>> {

	public static DispatchBiEntityCondition target() {
		return new DispatchBiEntityCondition((actor, target) -> target);
	}

	public static DispatchBiEntityCondition actor() {
		return new DispatchBiEntityCondition((actor, target) -> actor);
	}

	public static DispatchBiEntityCondition either() {
		return new DispatchBiEntityCondition((condition, actor, target) -> condition.check(actor) || condition.check(target));
	}

	public static DispatchBiEntityCondition both() {
		return new DispatchBiEntityCondition((condition, actor, target) -> condition.check(actor) && condition.check(target));
	}

	private final Operator operator;

	private DispatchBiEntityCondition(Operator operator) {
		super(FieldConfiguration.codec(ConfiguredEntityCondition.CODEC, "condition"));
		this.operator = operator;
	}

	private DispatchBiEntityCondition(BinaryOperator<Entity> operator) {
		this((condition, actor, target) -> condition.check(operator.apply(actor, target)));
	}

	@Override
	protected boolean check(FieldConfiguration<ConfiguredEntityCondition<?, ?>> configuration, Entity actor, Entity target) {
		return this.operator.check(configuration.value(), actor, target);
	}

	@FunctionalInterface
	public interface Operator {
		boolean check(ConfiguredEntityCondition<?, ?> condition, Entity actor, Entity target);
	}
}
