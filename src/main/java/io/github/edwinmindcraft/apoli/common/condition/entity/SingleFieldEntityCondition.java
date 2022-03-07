package io.github.edwinmindcraft.apoli.common.condition.entity;

import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiPredicate;

public class SingleFieldEntityCondition<T> extends EntityCondition<FieldConfiguration<T>> {
	public static boolean checkPredicate(LivingEntity entity, ResourceLocation identifier) {
		MinecraftServer server = entity.level.getServer();
		if (server != null) {
			LootItemCondition lootCondition = server.getPredicateManager().get(identifier);
			if (lootCondition != null) {
				LootContext.Builder lootBuilder = (new LootContext.Builder((ServerLevel) entity.level))
						.withParameter(LootContextParams.ORIGIN, entity.position())
						.withOptionalParameter(LootContextParams.THIS_ENTITY, entity);
				return lootCondition.test(lootBuilder.create(LootContextParamSets.COMMAND));
			}
		}
		return false;
	}

	private final BiPredicate<LivingEntity, T> predicate;

	public SingleFieldEntityCondition(MapCodec<T> codec, BiPredicate<LivingEntity, T> predicate) {
		super(FieldConfiguration.codec(codec));
		this.predicate = predicate;
	}

	@Override
	public boolean check(FieldConfiguration<T> configuration, LivingEntity entity) {
		return this.predicate.test(entity, configuration.value());
	}
}
