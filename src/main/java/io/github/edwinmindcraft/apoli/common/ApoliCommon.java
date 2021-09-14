package io.github.edwinmindcraft.apoli.common;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.common.network.C2SPlayerLandedPacket;
import io.github.edwinmindcraft.apoli.common.network.C2SUseActivePowers;
import io.github.edwinmindcraft.apoli.common.network.S2CSynchronizePowerContainer;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import io.github.edwinmindcraft.apoli.common.registry.action.ModBlockActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ModEntityActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ModItemActions;
import io.github.edwinmindcraft.apoli.common.registry.condition.*;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class ApoliCommon {
	public static final String NETWORK_VERSION = "1.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(Apoli.identifier("channel"))
			.networkProtocolVersion(() -> NETWORK_VERSION)
			.clientAcceptedVersions(NETWORK_VERSION::equals)
			.serverAcceptedVersions(NETWORK_VERSION::equals)
			.simpleChannel();

	private static void initializeNetwork() {
		int messageId = 0;
		CHANNEL.messageBuilder(C2SPlayerLandedPacket.class, messageId++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(C2SPlayerLandedPacket::encode).decoder(C2SPlayerLandedPacket::decode)
				.consumer(C2SPlayerLandedPacket::handle).add();

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
		ApoliRegisters.register();

		//Powers
		ModPowers.register();

		//Actions
		ModBlockActions.register();
		ModEntityActions.register();
		ModItemActions.register();

		//Conditions
		ModBiomeConditions.register();
		ModBlockConditions.register();
		ModDamageConditions.register();
		ModEntityConditions.register();
		ModFluidConditions.register();
		ModItemConditions.register();
	}
}
