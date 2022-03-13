package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.BiEntityConditionConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BiEntityConditionPower extends PowerFactory<BiEntityConditionConfiguration> {
	public static boolean any(BiEntityConditionPower power, Entity holder, Entity actor, Entity target) {
		return IPowerContainer.getPowers(holder, power).stream().anyMatch(x -> ConfiguredBiEntityCondition.check(x.getConfiguration().biEntityCondition(), actor, target));
	}

	public BiEntityConditionPower() {
		super(BiEntityConditionConfiguration.CODEC);
	}
}
