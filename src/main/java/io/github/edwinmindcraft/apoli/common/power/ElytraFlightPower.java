package io.github.edwinmindcraft.apoli.common.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.UUID;

public class ElytraFlightPower extends PowerFactory<FieldConfiguration<Boolean>> {
	public static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(UUID.fromString("29eb14ca-c803-4af6-81e2-86e9bf1d4857"), "Elytra modifier", 1.0F, AttributeModifier.Operation.ADDITION);

	public static boolean shouldRenderElytra(LivingEntity player) {
		return IPowerContainer.getPowers(player, ApoliPowers.ELYTRA_FLIGHT.get()).stream().anyMatch(x -> x.getConfiguration().value());
	}

	public static void enableFlight(LivingEntity player) {
		if (player.getAttributes().hasAttribute(CaelusApi.getInstance().getFlightAttribute())) {
			AttributeInstance attributeInstance = player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
			if (!attributeInstance.hasModifier(FLIGHT_MODIFIER))
				attributeInstance.addTransientModifier(FLIGHT_MODIFIER);
		}
	}

	public static void disableFlight(LivingEntity player) {
		if (player.getAttributes().hasAttribute(CaelusApi.getInstance().getFlightAttribute())) {
			AttributeInstance attributeInstance = player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
			if (attributeInstance.hasModifier(FLIGHT_MODIFIER))
				attributeInstance.removeModifier(FLIGHT_MODIFIER);
		}
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

	@Override
	protected void onRemoved(FieldConfiguration<Boolean> configuration, LivingEntity entity) {
		disableFlight(entity);
	}
}
