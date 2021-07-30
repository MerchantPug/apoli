package io.github.apace100.apoli.power;

import io.github.apace100.apoli.Apoli;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CreativeFlightPower extends Power {

    public CreativeFlightPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    @Override
    public void onAdded() {
        if(entity instanceof Player) {
            Apoli.SCHEDULER.queue(server -> {
                Apoli.POWER_SOURCE.grantTo((Player)entity, VanillaAbilities.ALLOW_FLYING);
                Apoli.POWER_SOURCE.grantTo((Player)entity, VanillaAbilities.FLYING);
            }, 1);
        }
    }

    @Override
    public void onRemoved() {
        if(entity instanceof Player) {
            Apoli.POWER_SOURCE.revokeFrom((Player)entity, VanillaAbilities.ALLOW_FLYING);
            Apoli.POWER_SOURCE.revokeFrom((Player)entity, VanillaAbilities.FLYING);
        }
    }
}
