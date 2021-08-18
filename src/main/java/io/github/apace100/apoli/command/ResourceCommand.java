package io.github.apace100.apoli.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Score;
import net.minecraftforge.common.util.LazyOptional;

import java.util.OptionalInt;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ResourceCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				literal("resource").requires(cs -> cs.hasPermission(2))
						.then(literal("has")
								.then(argument("target", EntityArgument.player())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> resource(command, SubCommand.HAS))))
						)
						.then(literal("get")
								.then(argument("target", EntityArgument.player())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> resource(command, SubCommand.GET))))
						)
						.then(literal("set")
								.then(argument("target", EntityArgument.player())
										.then(argument("power", PowerTypeArgumentType.power())
												.then(argument("value", IntegerArgumentType.integer())
														.executes((command) -> resource(command, SubCommand.SET)))))
						)
						.then(literal("change")
								.then(argument("target", EntityArgument.player())
										.then(argument("power", PowerTypeArgumentType.power())
												.then(argument("value", IntegerArgumentType.integer())
														.executes((command) -> resource(command, SubCommand.CHANGE)))))
						)
						.then(literal("operation")
								.then(argument("target", EntityArgument.player())
										.then(argument("power", PowerTypeArgumentType.power())
												.then(argument("operation", PowerOperation.operation())
														.then(argument("entity", ScoreHolderArgument.scoreHolder())
																.then(argument("objective", ObjectiveArgument.objective())
																		.executes((command) -> resource(command, SubCommand.OPERATION)))))))
						)
		);
	}

	private static int extract(OptionalInt optional, CommandContext<CommandSourceStack> command, IntFunction<Component> success, Supplier<Component> failure, boolean flatten) {
		optional.ifPresentOrElse(i -> command.getSource().sendSuccess(success.apply(i), true), () -> command.getSource().sendFailure(failure.get()));
		return flatten ? optional.isPresent() ? 1 : 0 : optional.orElse(0);
	}

	private static OptionalInt operation(CommandContext<CommandSourceStack> command, ConfiguredPower<?, ?> configuredPower, ServerPlayer player) throws CommandSyntaxException {
		Score score = command.getSource().getServer().getScoreboard().getOrCreatePlayerScore(ScoreHolderArgument.getName(command, "entity"), ObjectiveArgument.getObjective(command, "objective"));
		command.getArgument("operation", PowerOperation.Operation.class).apply(player, configuredPower, score);
		return configuredPower.getValue(player);
	}

	// This is a cleaner method than sticking it into every subcommand
	private static int resource(CommandContext<CommandSourceStack> command, SubCommand sub) throws CommandSyntaxException {
		ServerPlayer player = EntityArgument.getPlayer(command, "target");
		LazyOptional<IPowerContainer> optional = IPowerContainer.get(player);
		ResourceLocation power = command.getArgument("power", ResourceLocation.class);
		ConfiguredPower<?, ?> configuredPower = PowerTypeArgumentType.getConfiguredPower(command, "power");
		OptionalInt result = switch (sub) {
			case HAS -> optional.map(x -> x.hasPower(power) ? OptionalInt.of(1) : OptionalInt.empty()).orElse(OptionalInt.empty());
			case GET -> configuredPower.getValue(player);
			case SET -> configuredPower.assign(player, IntegerArgumentType.getInteger(command, "value"));
			case CHANGE -> configuredPower.change(player, IntegerArgumentType.getInteger(command, "value"));
			case OPERATION -> operation(command, configuredPower, player);
		};
		IntFunction<Component> success = switch (sub) {
			case HAS -> i -> new TranslatableComponent("commands.execute.conditional.pass");
			case GET -> i -> new TranslatableComponent("commands.scoreboard.players.get.success", player.getScoreboardName(), i, power);
			case SET -> i -> new TranslatableComponent("commands.scoreboard.players.set.success.single", power, player.getScoreboardName(), i);
			case CHANGE -> i -> new TranslatableComponent("commands.scoreboard.players.add.success.single", IntegerArgumentType.getInteger(command, "value"), power, player.getScoreboardName(), i);
			case OPERATION -> i -> new TranslatableComponent("commands.scoreboard.players.operation.success.single", power, player.getScoreboardName(), i);
		};
		Supplier<Component> failure = switch (sub) {
			case HAS -> () -> new TranslatableComponent("commands.execute.conditional.fail");
			case GET -> () -> new TranslatableComponent("commands.scoreboard.players.get.null", power, player.getScoreboardName());
			case SET, CHANGE, OPERATION -> () -> new TranslatableComponent("argument.scoreHolder.empty");
		};
		return extract(result, command, success, failure, sub != SubCommand.GET);
	}

	public enum SubCommand {
		HAS, GET, SET, CHANGE, OPERATION
	}
}