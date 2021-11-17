package io.github.edwinmindcraft.apoli.common.condition.entity;

import com.mojang.serialization.MapCodec;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiPredicate;

public class SubmergedInCondition extends EntityCondition<TagConfiguration<Fluid>> {

	public SubmergedInCondition() {
		super(TagConfiguration.codec(SerializableDataTypes.FLUID_TAG, "fluid"));
	}

	@Override
	public boolean check(TagConfiguration<Fluid> configuration, LivingEntity entity) {
		return configuration.isLoaded() && entity.isEyeInFluid(configuration.value());
	}
}
