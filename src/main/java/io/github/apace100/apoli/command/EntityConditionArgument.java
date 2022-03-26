package io.github.apace100.apoli.command;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.commands.CommandSourceStack;

import java.io.IOException;
import java.util.Collection;

public class EntityConditionArgument extends JsonObjectArgument<ConfiguredEntityCondition<?, ?>> {
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().registerTypeHierarchyAdapter(ConfiguredEntityCondition.class, CalioCodecHelper.jsonAdapter(ConfiguredEntityCondition.CODEC)).create();

	public static EntityConditionArgument entityCondition() {
		return new EntityConditionArgument();
	}

	public static ConfiguredEntityCondition<?, ?> getEntityCondition(CommandContext<CommandSourceStack> pContext, String pName) throws CommandSyntaxException {
		return pContext.getArgument(pName, ConfiguredEntityCondition.class);
	}

	@Override
	protected ConfiguredEntityCondition<?, ?> convert(JsonReader obj) throws IOException {
		return GSON.getAdapter(ConfiguredEntityCondition.class).read(obj);
	}

	@Override
	public Collection<String> getExamples() {
		return ImmutableSet.of("{\"type\":\"apoli:exposed_to_sky\"}");
	}
}
