package io.github.apace100.apoli.power;

import io.github.apace100.apoli.Apoli;
import io.github.ladysnake.pal.PlayerAbility;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerAbilityPower extends Power {

    private final PlayerAbility ability;

    public PlayerAbilityPower(PowerType<?> type, LivingEntity entity, PlayerAbility playerAbility) {
        super(type, entity);
        this.ability = playerAbility;
        if(entity instanceof Player) {
            this.setTicking(true);
        }
    }

    @Override
    public void tick() {
        if(!entity.level.isClientSide) {
            boolean isActive = isActive();
            boolean hasAbility = hasAbility();
            if(isActive && !hasAbility) {
                grantAbility();
            } else if(!isActive && hasAbility) {
                revokeAbility();
            }
        }
    }

    @Override
    public void onGained() {
        if(!entity.level.isClientSide && isActive() && !hasAbility()) {
            grantAbility();
        }
    }

    @Override
    public void onLost() {
        if(!entity.level.isClientSide && hasAbility()) {
            revokeAbility();
        }
    }

    public boolean hasAbility() {
        return Apoli.POWER_SOURCE.grants((Player)entity, ability);
    }

    public void grantAbility() {
        //Apoli.SCHEDULER.queue(server -> {
            Apoli.POWER_SOURCE.grantTo((Player)entity, ability);
         //   Apoli.POWER_SOURCE.grantTo((PlayerEntity)entity, VanillaAbilities.FLYING);
        //}, 1);
    }

    public void revokeAbility() {
        Apoli.POWER_SOURCE.revokeFrom((Player)entity, ability);
    }
}
