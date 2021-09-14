package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.AttributeModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ValueModifyingPowerConfiguration;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class ModifySwimSpeedPower extends AttributeModifyingPowerFactory<ValueModifyingPowerConfiguration> {

	public ModifySwimSpeedPower() {
		super(ValueModifyingPowerConfiguration.CODEC);
	}

	@Override
	public @Nullable Attribute getAttribute() {
		//FIXME Forge's swim speed doesn't have the same base value.
		return ForgeMod.SWIM_SPEED.get();
	}
}
