package io.github.edwinmindcraft.apoli.common.action.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;

public class MergeNbtItemAction extends ItemAction<FieldConfiguration<String>> {
    public MergeNbtItemAction() {
        super(FieldConfiguration.codec(SerializableDataTypes.STRING, "nbt"));
    }

    @Override
    public void execute(FieldConfiguration<String> configuration, Level level, Mutable<ItemStack> stack) {
        String nbtString = configuration.value();
        try {
            CompoundTag compound = new TagParser(new StringReader(nbtString)).readStruct();
            stack.getValue().getOrCreateTag().merge(compound);
        } catch (CommandSyntaxException e) {
            Apoli.LOGGER.error("Failed `merge_nbt` item action due to malformed nbt string: \"" + nbtString + "\"");
        }
    }
}
