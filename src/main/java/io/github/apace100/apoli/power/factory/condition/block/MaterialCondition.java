package io.github.apace100.apoli.power.factory.condition.block;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class MaterialCondition extends BlockCondition<ListConfiguration<Material>> {

	public MaterialCondition() {
		super(ListConfiguration.codec(SerializableDataTypes.MATERIAL, "material", "materials"));
	}

	@Override
	protected boolean check(@NotNull ListConfiguration<Material> configuration, @NotNull LevelReader reader, @NotNull BlockPos position, @NotNull NonNullSupplier<BlockState> stateGetter) {
		return configuration.getContent().stream().anyMatch(stateGetter.get().getMaterial()::equals);
	}
}
