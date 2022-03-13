package io.github.apace100.apoli.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ApoliConfigClient {

	public ApoliConfigClient(ForgeConfigSpec.Builder builder) {
		builder.push("resources_and_cooldowns");
		this.resourcesAndCooldowns = new ResourcesAndCooldowns(builder);
		builder.pop();
		builder.push("tooltips");
		this.tooltips = new Tooltips(builder);
		builder.pop();
	}

	public final ResourcesAndCooldowns resourcesAndCooldowns;

	public final Tooltips tooltips;

	public static class ResourcesAndCooldowns {

		public final ForgeConfigSpec.ConfigValue<Integer> hudOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> hudOffsetY;

		public ResourcesAndCooldowns(ForgeConfigSpec.Builder builder) {
			this.hudOffsetX = builder
					.translation("text.autoconfig.power_config.option.resourcesAndCooldowns.hudOffsetX")
					.define("hud_offset_x", 0);
			this.hudOffsetY = builder
					.translation("text.autoconfig.power_config.option.resourcesAndCooldowns.hudOffsetY")
					.define("hud_offset_y", 0);
		}
	}

	public static class Tooltips {

		public final ForgeConfigSpec.BooleanValue showUsabilityHints;
		public final ForgeConfigSpec.BooleanValue compactUsabilityHints;

		public Tooltips(ForgeConfigSpec.Builder builder) {
			this.showUsabilityHints = builder
					.translation("text.autoconfig.power_config.option.tooltips.showUsabilityHints")
					.define("show_usability_hints", true);
			this.compactUsabilityHints = builder
					.translation("text.autoconfig.power_config.option.tooltips.compactUsabilityHints")
					.define("compact_usability_hints", false);
		}
	}
}
