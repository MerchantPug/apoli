package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.EntityGroup;

public class EntityGroupPower extends PowerFactory<FieldConfiguration<EntityGroup>> {

	public EntityGroupPower() {
		super(FieldConfiguration.codec(SerializableDataTypes.ENTITY_GROUP, "group"));
	}
}
