package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.LivingEntity;

//FIXME Requires Architectury
public class ElytraFlightPower extends PowerFactory<FieldConfiguration<Boolean>> {
	public static boolean shouldRenderElytra(LivingEntity player) {
		return IPowerContainer.getPowers(player, ModPowers.ELYTRA_FLIGHT.get()).stream().anyMatch(x -> x.getConfiguration().value());
	}

	//@ExpectPlatform
	public static void enableFlight(LivingEntity player) {
		throw new AssertionError();
	}

	//@ExpectPlatform
	public static void disableFlight(LivingEntity player) {
		throw new AssertionError();
	}

	public ElytraFlightPower() {
		super(FieldConfiguration.codec(Codec.BOOL, "render_elytra"));
		this.ticking(true);
	}

	@Override
	public void tick(ConfiguredPower<FieldConfiguration<Boolean>, ?> configuration, LivingEntity player) {
		if (configuration.isActive(player))
			enableFlight(player);
		else
			disableFlight(player);
	}
}
