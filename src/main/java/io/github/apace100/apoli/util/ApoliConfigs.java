package io.github.apace100.apoli.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ApoliConfigs {

	public static final ForgeConfigSpec COMMON_SPECS;
	public static final ForgeConfigSpec CLIENT_SPECS;
	public static final ForgeConfigSpec SERVER_SPECS;

	public static final ApoliConfig COMMON;
	public static final ApoliConfigClient CLIENT;
	public static final ApoliConfigServer SERVER;

	static {
		final Pair<ApoliConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ApoliConfig::new);
		COMMON_SPECS = common.getRight();
		COMMON = common.getLeft();
		final Pair<ApoliConfigClient, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(ApoliConfigClient::new);
		CLIENT_SPECS = client.getRight();
		CLIENT = client.getLeft();
		final Pair<ApoliConfigServer, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(ApoliConfigServer::new);
		SERVER_SPECS = server.getRight();
		SERVER = server.getLeft();
	}
}
