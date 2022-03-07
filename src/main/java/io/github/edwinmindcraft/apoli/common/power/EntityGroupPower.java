package io.github.edwinmindcraft.apoli.common.power;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.MobType;

public class EntityGroupPower extends PowerFactory<FieldConfiguration<MobType>> {

	public EntityGroupPower() {
		super(FieldConfiguration.codec(SerializableDataTypes.ENTITY_GROUP, "group"));
	}
}
