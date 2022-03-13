package io.github.edwinmindcraft.apoli.common.action.block;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class NbtCondition extends BlockCondition<FieldConfiguration<CompoundTag>> {
	public NbtCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.NBT, "nbt"));
	}

	@Override
	protected boolean check(FieldConfiguration<CompoundTag> configuration, BlockInWorld block) {
		CompoundTag nbt = new CompoundTag();
		if (block.getEntity() != null) {
			nbt = block.getEntity().serializeNBT();
		}
		return NbtUtils.compareNbt(configuration.value(), nbt, true);
	}
}
