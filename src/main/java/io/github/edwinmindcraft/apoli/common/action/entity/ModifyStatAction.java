package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.action.configuration.ModifyStatConfiguration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.entity.Entity;

public class ModifyStatAction extends EntityAction<ModifyStatConfiguration> {
    public ModifyStatAction() {
        super(ModifyStatConfiguration.CODEC);
    }

    @Override
    public void execute(ModifyStatConfiguration configuration, Entity entity) {
        if (!(entity instanceof ServerPlayer serverPlayerEntity)) return;

        ServerStatsCounter serverStatHandler = serverPlayerEntity.getStats();

        int newValue;
        int originalValue = serverStatHandler.getValue(configuration.stat());

        serverPlayerEntity.resetStat(configuration.stat());

        newValue = (int) ConfiguredModifier.apply(configuration.modifier(), entity, originalValue);

        serverPlayerEntity.awardStat(configuration.stat(), newValue);
    }
}