package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModifyDeathTicksAction extends EntityAction<FieldConfiguration<Holder<ConfiguredModifier<?>>>> {

    public ModifyDeathTicksAction() {
        super(FieldConfiguration.codec(ConfiguredModifier.required("modifier")));
    }

    @Override
    public void execute(FieldConfiguration<Holder<ConfiguredModifier<?>>> configuration, Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.deathTime = (int) ConfiguredModifier.apply(configuration.value(), entity, living.deathTime);
        }
    }
}
