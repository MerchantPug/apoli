package io.github.edwinmindcraft.apoli.common.action.bientity;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

public class DispatchBiEntityAction extends BiEntityAction<FieldConfiguration<ConfiguredEntityAction<?, ?>>> {

	public static DispatchBiEntityAction target() {
		return new DispatchBiEntityAction((actor, target) -> target);
	}
	public static DispatchBiEntityAction actor() {
		return new DispatchBiEntityAction((actor, target) -> actor);
	}

	private final BinaryOperator<Entity> selector;

	private DispatchBiEntityAction(BinaryOperator<Entity> selector) {
		super(FieldConfiguration.codec(ConfiguredEntityAction.CODEC, "action"));
		this.selector = selector;
	}

	@Override
	public void execute(FieldConfiguration<ConfiguredEntityAction<?, ?>> configuration, Entity actor, Entity target) {
		configuration.value().execute(this.selector.apply(actor, target));
	}
}
