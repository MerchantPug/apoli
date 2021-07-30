package io.github.apace100.apoli.power;

import io.github.apace100.apoli.util.HudRender;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ActiveCooldownPower extends CooldownPower implements Active {

    private final Consumer<Entity> activeFunction;

    public ActiveCooldownPower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender);
        this.activeFunction = activeFunction;
    }

    @Override
    public void onUse() {
        if(canUse()) {
            this.activeFunction.accept(this.entity);
            use();
        }
    }

    private Key key;

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public void setKey(Key key) {
        this.key = key;
    }
}
