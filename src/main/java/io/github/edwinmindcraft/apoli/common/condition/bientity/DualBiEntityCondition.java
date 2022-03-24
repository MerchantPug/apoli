package io.github.edwinmindcraft.apoli.common.condition.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.HolderConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

public class DualBiEntityCondition extends BiEntityCondition<HolderConfiguration<ConfiguredBiEntityCondition<?, ?>>> {

	public static DualBiEntityCondition invert() {
		return new DualBiEntityCondition((condition, actor, target) -> ConfiguredBiEntityCondition.check(condition, target, actor));
	}

	public static DualBiEntityCondition undirected() {
		return new DualBiEntityCondition((condition, actor, target) -> ConfiguredBiEntityCondition.check(condition, actor, target) || ConfiguredBiEntityCondition.check(condition, target, actor));
	}

	private final Operator operator;

	private DualBiEntityCondition(Operator operator) {
		super(HolderConfiguration.required(ConfiguredBiEntityCondition.required("condition")));
		this.operator = operator;
	}

	@Override
	protected boolean check(HolderConfiguration<ConfiguredBiEntityCondition<?, ?>> configuration, Entity actor, Entity target) {
		return this.operator.check(configuration.holder(), actor, target);
	}

	@FunctionalInterface
	public interface Operator {
		boolean check(Holder<ConfiguredBiEntityCondition<?, ?>> condition, Entity actor, Entity target);
	}
}
