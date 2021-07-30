package dev.experimental.apoli.common.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.registry.ModPowers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

//FIXME Requires Architectury
public class ElytraFlightPower extends PowerFactory<FieldConfiguration<Boolean>> {
	public static boolean shouldRenderElytra(Player player) {
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
