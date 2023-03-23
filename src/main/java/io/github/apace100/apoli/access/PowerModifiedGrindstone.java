package io.github.apace100.apoli.access;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyGrindstonePower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyGrindstoneConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public interface PowerModifiedGrindstone {
    List<Holder<ConfiguredPower<ModifyGrindstoneConfiguration, ModifyGrindstonePower>>> getAppliedPowers();

    Player getPlayer();

    Optional<BlockPos> getPos();
}