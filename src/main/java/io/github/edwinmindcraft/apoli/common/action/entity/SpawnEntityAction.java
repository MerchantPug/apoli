package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.SpawnEntityConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

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
			Apoli.LOGGER.error("Failed to create entity for type: {}", ForgeRegistries.ENTITY_TYPES.getKey(configuration.type()));
			return;
		}
		newEntity.absMoveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
		if (configuration.tag() != null) {
			CompoundTag tag = newEntity.saveWithoutId(new CompoundTag());
			tag.merge(configuration.tag());
			newEntity.load(tag);
		}
		entity.getCommandSenderWorld().addFreshEntity(newEntity);
		ConfiguredEntityAction.execute(configuration.action(), newEntity);
	}
}
