package io.github.edwinmindcraft.apoli.common.action.bientity;

import io.github.edwinmindcraft.apoli.api.configuration.HolderConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import net.minecraft.world.entity.Entity;

import java.util.function.BinaryOperator;

public class DispatchBiEntityAction extends BiEntityAction<HolderConfiguration<ConfiguredEntityAction<?, ?>>> {

	public static DispatchBiEntityAction target() {
		return new DispatchBiEntityAction((actor, target) -> target);
	}

	public static DispatchBiEntityAction actor() {
		return new DispatchBiEntityAction((actor, target) -> actor);
	}

	private final BinaryOperator<Entity> selector;

	private DispatchBiEntityAction(BinaryOperator<Entity> selector) {
		super(HolderConfiguration.required(ConfiguredEntityAction.required("action")));
		this.selector = selector;
	}

	@Override
	public void execute(HolderConfiguration<ConfiguredEntityAction<?, ?>> configuration, Entity actor, Entity target) {
		ConfiguredEntityAction.execute(configuration.holder(), this.selector.apply(actor, target));
	}
}
