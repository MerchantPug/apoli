package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.SpawnEntityConfiguration;
import io.github.apace100.apoli.Apoli;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class SpawnEntityAction extends EntityAction<SpawnEntityConfiguration> {

	public SpawnEntityAction() {
		super(SpawnEntityConfiguration.CODEC);
	}

	@Override
	public void execute(SpawnEntityConfiguration configuration, Entity entity) {
		if (configuration.type() == null)
			return;
		Entity newEntity = configuration.type().create(entity.getCommandSenderWorld());
		if (newEntity == null) {
			Apoli.LOGGER.error("Failed to create entity for type: {}", Registry.ENTITY_TYPE.getKey(configuration.type()));
			return;
		}
		if (configuration.tag() != null) {
			CompoundTag tag = newEntity.saveWithoutId(new CompoundTag());
			tag.merge(configuration.tag());
			newEntity.load(tag);
		}
		entity.getCommandSenderWorld().addFreshEntity(newEntity);
		ConfiguredEntityAction.execute(configuration.action(), newEntity);
	}
}
