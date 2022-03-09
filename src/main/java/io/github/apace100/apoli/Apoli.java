package io.github.apace100.apoli;

import io.github.edwinmindcraft.apoli.client.ApoliClient;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.apace100.apoli.command.PowerOperation;
import io.github.apace100.apoli.command.PowerTypeArgumentType;
import io.github.apace100.apoli.util.Scheduler;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Apoli.MODID)
public class Apoli {

	public static final Scheduler SCHEDULER = new Scheduler();

	public static final String MODID = "apoli";
	public static final Logger LOGGER = LogManager.getLogger(Apoli.class);
	public static String VERSION = "";
	public static int[] SEMVER;

	//public static final AbilitySource LEGACY_POWER_SOURCE = Pal.getAbilitySource(Apoli.identifier("power_source"));

	public static final boolean PERFORM_VERSION_CHECK = false;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(s -> server = s);

		FabricLoader.getInstance().getModContainer(MODID).ifPresent(modContainer -> {
			VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
			if(VERSION.contains("+")) {
				VERSION = VERSION.split("\\+")[0];
			}
			if(VERSION.contains("-")) {
				VERSION = VERSION.split("-")[0];
			}
			String[] splitVersion = VERSION.split("\\.");
			SEMVER = new int[splitVersion.length];
			for(int i = 0; i < SEMVER.length; i++) {
				SEMVER[i] = Integer.parseInt(splitVersion[i]);
			}
		});

		ModPacketsC2S.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			PowerCommand.register(dispatcher);
			ResourceCommand.register(dispatcher);
		});

		Registry.register(Registry.RECIPE_SERIALIZER, Apoli.identifier("power_restricted"), PowerRestrictedCraftingRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Apoli.identifier("modified"), ModifiedCraftingRecipe.SERIALIZER);

		Registry.register(Registry.LOOT_FUNCTION_TYPE, Apoli.identifier("add_power"), AddPowerLootFunction.TYPE);

		ArgumentTypes.register(MODID + ":power", PowerTypeArgumentType.class, new ConstantArgumentSerializer<>(PowerTypeArgumentType::power));
		ArgumentTypes.register(MODID + ":power_operation", PowerOperation.class, new ConstantArgumentSerializer<>(PowerOperation::operation));

		ApoliClassData.registerAll();

		PowerFactories.register();
		EntityConditions.register();
		BiEntityConditions.register();
		ItemConditions.register();
		BlockConditions.register();
		DamageConditions.register();
		FluidConditions.register();
		BiomeConditions.register();
		EntityActions.register();
		ItemActions.register();
		BlockActions.register();
		BiEntityActions.register();

		OrderedResourceListeners.register(new PowerTypes()).complete();

		CriteriaRegistryInvoker.callRegister(GainedPowerCriterion.INSTANCE);

		LOGGER.info("Apoli " + VERSION + " has initialized. Ready to power up your game!");
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, PowerHolderComponent.KEY)
			.impl(PowerHolderComponentImpl.class)
			.respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY)
			.end(PowerHolderComponentImpl::new);
	}
}
