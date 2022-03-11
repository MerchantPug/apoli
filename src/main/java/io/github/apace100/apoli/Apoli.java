package io.github.apace100.apoli;

import io.github.apace100.apoli.util.GainedPowerCriterion;
import io.github.apace100.calio.mixin.CriteriaRegistryInvoker;
import io.github.edwinmindcraft.apoli.client.ApoliClient;
import io.github.edwinmindcraft.apoli.common.ApoliCommon;
import io.github.apace100.apoli.command.PowerOperation;
import io.github.apace100.apoli.command.PowerTypeArgumentType;
import io.github.apace100.apoli.util.Scheduler;
import net.minecraft.advancements.CriteriaTriggers;
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
	//public static String VERSION = "";
	//public static int[] SEMVER;

	//public static final AbilitySource LEGACY_POWER_SOURCE = Pal.getAbilitySource(Apoli.identifier("power_source"));

	public static final boolean PERFORM_VERSION_CHECK = false;

	public static ResourceLocation identifier(String path) {
		return new ResourceLocation(MODID, path);
	}

	public Apoli() {
		ArgumentTypes.register(MODID + ":power", PowerTypeArgumentType.class, new EmptyArgumentSerializer<>(PowerTypeArgumentType::power));
		ArgumentTypes.register(MODID + ":power_operation", PowerOperation.class, new EmptyArgumentSerializer<>(PowerOperation::operation));

		//ModPacketsC2S.register();

		//Registry.register(Registry.RECIPE_SERIALIZER, Apoli.identifier("power_restricted"), PowerRestrictedCraftingRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Apoli.identifier("modified"), ModifiedCraftingRecipe.SERIALIZER);

		//OrderedResourceListeners.register(new PowerTypes()).complete();

		CriteriaTriggers.register(GainedPowerCriterion.INSTANCE);

		ApoliCommon.initialize();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ApoliClient::initialize);
		LOGGER.info("Apoli " + ModLoadingContext.get().getActiveContainer().getModInfo().getVersion() + " has initialized. Ready to power up your game!");
	}
}
