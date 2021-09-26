package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.AttributeModifyingPowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

//FIXME Forge has a gravity attribute. I need to use that.
public class ModifyFallingPower extends AttributeModifyingPowerFactory<ModifyFallingConfiguration> {
	public ModifyFallingPower() {
		super(ModifyFallingConfiguration.CODEC);
	}

	@Override
	public @Nullable Attribute getAttribute() {
		return ForgeMod.ENTITY_GRAVITY.get();
	}
}
