package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModifyDeathTicksAction extends EntityAction<FieldConfiguration<ListConfiguration<ConfiguredModifier<?>>>> {

    public ModifyDeathTicksAction() {
        super(FieldConfiguration.codec(ListConfiguration.MODIFIER_CODEC));
    }

    @Override
    public void execute(FieldConfiguration<ListConfiguration<ConfiguredModifier<?>>> configuration, Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.deathTime = (int) ModifierUtil.applyModifiers(entity, configuration.value().entries(), living.deathTime);
        }
    }
}
