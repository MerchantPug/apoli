package dev.experiment.helper;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum ExperimentEnforcer {
	HUD("hud");

	public static final Logger LOGGER = LogUtils.getLogger();

	private final Set<String> enforcers = ConcurrentHashMap.newKeySet();
	private final String name;

	ExperimentEnforcer(String name) {
		this.name = name;
	}

	public void enforce() {
		ModContainer container = ModLoadingContext.get().getActiveContainer();
		if (container == null || container.getCurrentState().ordinal() > ModLoadingStage.COMMON_SETUP.ordinal())
			throw new IllegalStateException("Cannot enforce an experiment outside of a mod's initialization phase.");
		this.enforcers.add(ModLoadingContext.get().getActiveNamespace());
	}

	public boolean isEnforced() {
		return !this.enforcers.isEmpty();
	}

	public static void log() {
		for (ExperimentEnforcer value : ExperimentEnforcer.values()) {
			if (value.isEnforced()) {
				LOGGER.info("Experiment \"{}\" was enforced by mods [{}]", value.name, String.join(",", value.enforcers));
			}
		}
	}
}
