package io.github.apace100.apoli.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Score;

import java.util.concurrent.CompletableFuture;

// Very similar to OperationArgumentType, but modified to make it work with resources.
public class PowerOperation implements ArgumentType<PowerOperation.Operation> {
	public static final SimpleCommandExceptionType INVALID_OPERATION = new SimpleCommandExceptionType(new TranslatableComponent("arguments.operation.invalid"));
	public static final SimpleCommandExceptionType DIVISION_ZERO_EXCEPTION = new SimpleCommandExceptionType(new TranslatableComponent("arguments.operation.div0"));

	public static PowerOperation operation() {
		return new PowerOperation();
	}

	public PowerOperation.Operation parse(StringReader stringReader) throws CommandSyntaxException {
		if (!stringReader.canRead()) throw INVALID_OPERATION.create();

		int i = stringReader.getCursor();
		while (stringReader.canRead() && stringReader.peek() != ' ') stringReader.skip();

		String stringOperator = stringReader.getString().substring(i, stringReader.getCursor());
		return switch (stringOperator) {
			case "=" -> BuiltinOperation.SET;
			case "+=" -> BuiltinOperation.ADD;
			case "-=" -> BuiltinOperation.SUBTRACT;
			case "*=" -> BuiltinOperation.MULTIPLY;
			case "/=" -> BuiltinOperation.DIVIDE;
			case "%=" -> BuiltinOperation.MODULUS;
			case "<" -> BuiltinOperation.MIN;
			case ">" -> BuiltinOperation.MAX;
			case "><" -> BuiltinOperation.SWAP;
			default -> throw INVALID_OPERATION.create();
		};
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(new String[]{"=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><"}, builder);
	}

	private enum BuiltinOperation implements Operation {
		SET((living, power, score) -> power.assign(living, score.getScore())),
		ADD((living, power, score) -> power.change(living, score.getScore())),
		SUBTRACT((living, power, score) -> power.change(living, -score.getScore())),
		MULTIPLY((living, power, score) -> power.getValue(living).ifPresent(current -> power.assign(living, current * score.getScore()))),
		DIVIDE((living, power, score) -> {
			int val = score.getScore();
			if (val == 0) throw DIVISION_ZERO_EXCEPTION.create();
			power.getValue(living).ifPresent(current -> power.assign(living, Math.floorDiv(current, val)));
		}),
		MODULUS((living, power, score) -> {
			int val = score.getScore();
			if (val == 0) throw DIVISION_ZERO_EXCEPTION.create();
			power.getValue(living).ifPresent(current -> power.assign(living, Math.floorMod(current, val)));
		}),
		MIN((living, power, score) -> power.getValue(living).ifPresent(current -> power.assign(living, Math.min(current, score.getScore())))),
		MAX((living, power, score) -> power.getValue(living).ifPresent(current -> power.assign(living, Math.min(current, score.getScore())))),
		SWAP((living, power, score) -> power.getValue(living).ifPresent(current -> {
			power.assign(living, score.getScore());
			score.setScore(current);
		}));

		private final Operation operation;

		BuiltinOperation(Operation operation) {this.operation = operation;}

		@Override
		public void apply(LivingEntity living, ConfiguredPower<?, ?> power, Score score) throws CommandSyntaxException {
			this.operation.apply(living, power, score);
		}
	}

	public interface Operation {
		//FIXME void apply(Power power, Score score) throws CommandSyntaxException;
		void apply(LivingEntity living, ConfiguredPower<?, ?> power, Score score) throws CommandSyntaxException;
	}
}
