package dev.experiment.hud;

import dev.experiment.helper.ExperimentEnforcer;
import dev.experiment.helper.RegistryDefinition;
import dev.experiment.hud.factory.DefaultHudRenderer;
import io.github.apace100.apoli.screen.GameHudRender;
import io.github.apace100.apoli.util.ApoliConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

public class HudExperiment {

	public static boolean isEnabled() {
		return ExperimentEnforcer.HUD.isEnforced() || ApoliConfigs.COMMON.experiments.hud.get();
	}

	public static final RegistryDefinition<HudRendererFactory<?>> HUD_RENDERERS = RegistryDefinition.define("hud_renderer");

	public static final RegistryObject<DefaultHudRenderer> DEFAULT = HUD_RENDERERS.register().register("default", DefaultHudRenderer::new);

	public static void initialize() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		HUD_RENDERERS.register().register(bus);
	}

	@OnlyIn(Dist.CLIENT)
	public static void initializeClient() {
		GameHudRender.HUD_RENDERS.add(ConfiguredHudDrawer.INSTANCE);
	}
}
