package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyAttributeConfiguration;

public class ModifyAttributePower extends ValueModifyingPowerFactory<ModifyAttributeConfiguration> {

    public ModifyAttributePower() {
        super(ModifyAttributeConfiguration.CODEC);
    }
}
