package io.github.apace100.apoli.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ApoliConfig {
	public final Experiments experiments;

	public ApoliConfig(ForgeConfigSpec.Builder builder) {
		builder.push("experiments");
		this.experiments = new Experiments(builder);
		builder.pop();
	}

	public static class Experiments {
		public final ForgeConfigSpec.BooleanValue hud;

		public Experiments(ForgeConfigSpec.Builder builder) {
			this.hud = builder
					.comment("Enabled the hud experiment, adding custom hud renderers")
					.translation("config.apoli.experiment.hud")
					.define("hud", false);
		}
	}
}
