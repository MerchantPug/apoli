package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NbtCondition extends ItemCondition<FieldConfiguration<CompoundTag>> {
	public NbtCondition() {
		super(FieldConfiguration.codec(SerializableDataTypes.NBT, "nbt"));
	}

	@Override
	protected boolean check(FieldConfiguration<CompoundTag> configuration, @Nullable Level level, ItemStack stack) {
		return NbtUtils.compareNbt(configuration.value(), stack.getTag(), true);
	}
}
