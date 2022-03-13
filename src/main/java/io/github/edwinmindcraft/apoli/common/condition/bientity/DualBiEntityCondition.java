package io.github.edwinmindcraft.apoli.common.condition.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.world.entity.Entity;

public class DualBiEntityCondition extends BiEntityCondition<FieldConfiguration<ConfiguredBiEntityCondition<?, ?>>> {

	public static DualBiEntityCondition invert() {
		return new DualBiEntityCondition((condition, actor, target) -> condition.check(target, actor));
	}

	public static DualBiEntityCondition undirected() {
		return new DualBiEntityCondition((condition, actor, target) -> condition.check(actor, target) || condition.check(target, actor));
	}

	private final Operator operator;

	private DualBiEntityCondition(Operator operator) {
		super(FieldConfiguration.codec(ConfiguredBiEntityCondition.CODEC, "condition"));
		this.operator = operator;
	}

	@Override
	protected boolean check(FieldConfiguration<ConfiguredBiEntityCondition<?, ?>> configuration, Entity actor, Entity target) {
		return this.operator.check(configuration.value(), actor, target);
	}

	@FunctionalInterface
	public interface Operator {
		boolean check(ConfiguredBiEntityCondition<?, ?> condition, Entity actor, Entity target);
	}
}
