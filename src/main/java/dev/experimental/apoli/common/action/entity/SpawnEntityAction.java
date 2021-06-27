package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.configuration.ConfiguredEntityAction;
import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.SpawnEntityConfiguration;
import io.github.apace100.apoli.Apoli;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

public class SpawnEntityAction extends EntityAction<SpawnEntityConfiguration> {

	public SpawnEntityAction() {
		super(SpawnEntityConfiguration.CODEC);
	}

	@Override
	public void execute(SpawnEntityConfiguration configuration, Entity entity) {
		if (configuration.type() == null)
			return;
		Entity newEntity = configuration.type().create(entity.getEntityWorld());
		if (newEntity == null) {
			Apoli.LOGGER.error("Failed to create entity for type: {}", Registry.ENTITY_TYPE.getId(configuration.type()));
			return;
		}
		if (configuration.tag() != null) {
			NbtCompound tag = newEntity.writeNbt(new NbtCompound());
			tag.copyFrom(configuration.tag());
			newEntity.readNbt(tag);
		}
		entity.getEntityWorld().spawnEntity(newEntity);
		ConfiguredEntityAction.execute(configuration.action(), newEntity);
	}
}
