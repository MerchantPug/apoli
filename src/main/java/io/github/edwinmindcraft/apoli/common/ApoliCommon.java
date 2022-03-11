package io.github.edwinmindcraft.apoli.common;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.common.network.C2SUseActivePowers;
import io.github.edwinmindcraft.apoli.common.network.S2CSynchronizePowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ApoliLootFunctions;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRecipeSerializers;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliBlockActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliEntityActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliIBiEntityActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliItemActions;
import io.github.edwinmindcraft.apoli.common.registry.condition.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ApoliCommon {
	public static final String NETWORK_VERSION = "1.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(Apoli.identifier("channel"))
			.networkProtocolVersion(() -> NETWORK_VERSION)
			.clientAcceptedVersions(NETWORK_VERSION::equals)
			.serverAcceptedVersions(NETWORK_VERSION::equals)
			.simpleChannel();

	public static final ResourceLocation POWER_SOURCE = Apoli.identifier("power_source");

	private static void initializeNetwork() {
		int messageId = 0;
		/*CHANNEL.messageBuilder(C2SPlayerLandedPacket.class, messageId++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(C2SPlayerLandedPacket::encode).decoder(C2SPlayerLandedPacket::decode)
				.consumer(C2SPlayerLandedPacket::handle).add();*/

		CHANNEL.messageBuilder(C2SUseActivePowers.class, messageId++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(C2SUseActivePowers::encode).decoder(C2SUseActivePowers::decode)
				.consumer(C2SUseActivePowers::handle).add();

		CHANNEL.messageBuilder(S2CSynchronizePowerContainer.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
				.encoder(S2CSynchronizePowerContainer::encode).decoder(S2CSynchronizePowerContainer::decode)
				.consumer(S2CSynchronizePowerContainer::handle).add();

		Apoli.LOGGER.debug("Registered {} newtork messages.", messageId);
	}

	public static void initialize() {
		initializeNetwork();
		//Initialises registries.
		ApoliRegisters.initialize();

		//Vanilla stuff
		ApoliRecipeSerializers.bootstrap();

		//Powers
		ApoliPowers.bootstrap();

		//Actions
		ApoliBlockActions.bootstrap();
		ApoliEntityActions.bootstrap();
		ApoliItemActions.bootstrap();
		ApoliIBiEntityActions.bootstrap();

		//Conditions
		ApoliBiomeConditions.bootstrap();
		ApoliBlockConditions.bootstrap();
		ApoliDamageConditions.bootstrap();
		ApoliEntityConditions.bootstrap();
		ApoliFluidConditions.bootstrap();
		ApoliItemConditions.bootstrap();
		ApoliBiEntityConditions.bootstrap();

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(ApoliCommon::commonSetup);
	}

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(ApoliLootFunctions::bootstrap);
	}
}
