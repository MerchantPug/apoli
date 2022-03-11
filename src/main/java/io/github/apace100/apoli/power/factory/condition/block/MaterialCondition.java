package io.github.apace100.apoli.power.factory.condition.block;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.Material;

public class MaterialCondition extends BlockCondition<ListConfiguration<Material>> {

	public MaterialCondition() {
		super(ListConfiguration.codec(SerializableDataTypes.MATERIAL, "material", "materials"));
	}

	@Override
	protected boolean check(ListConfiguration<Material> configuration, BlockInWorld block) {
		return configuration.getContent().stream().anyMatch(block.getState().getMaterial()::equals);
	}
}
