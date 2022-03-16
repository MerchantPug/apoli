package io.github.apace100.apoli.power.factory.action.entity;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SwingHandAction extends EntityAction<FieldConfiguration<InteractionHand>> {

	public SwingHandAction() {
		super(FieldConfiguration.codec(SerializableDataTypes.HAND, "hand", InteractionHand.MAIN_HAND));
	}

	@Override
	public void execute(@NotNull FieldConfiguration<InteractionHand> configuration, @NotNull Entity entity) {
		if (entity instanceof LivingEntity living)
			living.swing(configuration.value(), true);
	}
}
