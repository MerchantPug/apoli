package io.github.apace100.apoli.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ApoliConfig {

	public final ExecuteCommand executeCommand;

	public ApoliConfig(ForgeConfigSpec.Builder builder) {
		builder.push("execute_command");
		this.executeCommand = new ExecuteCommand(builder);
		builder.pop();
	}

	public static class ExecuteCommand {
		public final ForgeConfigSpec.IntValue permissionLevel;
		public final ForgeConfigSpec.BooleanValue showOutput;

		public ExecuteCommand(ForgeConfigSpec.Builder builder) {
			this.permissionLevel = builder
					.translation("text.autoconfig.power_config.option.executeCommand.permissionLevel")
					.comment("Permission level used. 2 is enough for everything except server and debug commands.")
					.defineInRange("permission_level", 2, 0, 4);
			this.showOutput = builder
					.translation("text.autoconfig.power_config.option.executeCommand.showOutput")
					.comment("If true, this sends the command results in chat. Shouldn't be used during actual gameplay")
					.define("show_output", false);
		}
	}
}
