package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.factory.power.AttributeModifyingPowerFactory;
import dev.experimental.apoli.common.power.configuration.ValueModifyingPowerConfiguration;
import dev.experimental.apoli.common.registry.ModRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.Nullable;

public class ModifySwimSpeedPower extends AttributeModifyingPowerFactory<ValueModifyingPowerConfiguration> {

	public ModifySwimSpeedPower() {
		super(ValueModifyingPowerConfiguration.CODEC);
	}

	@Override
	public @Nullable Attribute getAttribute() {
		//Forge: ForgeMod#SWIM_SPEED_ATTRIBUTE
		//Fabric: null unless defined by another mod.
		//FIXME Forge's swim speed doesn't have the same base value.
		return ModRegistries.SWIM_SPEED.getOrNull();
	}
}
