package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import java.util.List;

import net.minecraft.world.entity.Entity;

public class InvisibilityPower extends PowerFactory<FieldConfiguration<Boolean>> {

	public static boolean isArmorHidden(Entity player) {
		List<ConfiguredPower<FieldConfiguration<Boolean>, InvisibilityPower>> powers = IPowerContainer.getPowers(player, ModPowers.INVISIBILITY.get());
		return !powers.isEmpty() && powers.stream().noneMatch(x -> x.getConfiguration().value());
	}

	public InvisibilityPower() {
		super(FieldConfiguration.codec(Codec.BOOL, "render_armor"));
	}
}
