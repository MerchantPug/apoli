package io.github.apace100.apoli.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.calio.api.CalioAPI;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class PowerTypeArgumentType implements ArgumentType<ResourceLocation> {

	public static PowerTypeArgumentType power() {
		return new PowerTypeArgumentType();
	}

	public static ConfiguredPower<?, ?> getConfiguredPower(CommandContext<CommandSourceStack> context, String argumentName) {
		ResourceLocation argument = context.getArgument(argumentName, ResourceLocation.class);
		return CalioAPI.getDynamicRegistries(context.getSource().getServer()).get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY)
				.getOptional(argument).orElseThrow(() -> new CommandRuntimeException(new TranslatableComponent("arguments.apoli.power_type.fail", argument)));
	}

	public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
		return ResourceLocation.read(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(ApoliAPI.getPowers().keySet(), builder);
	}
}
