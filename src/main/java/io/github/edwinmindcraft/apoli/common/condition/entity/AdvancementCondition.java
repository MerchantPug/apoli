package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class AdvancementCondition extends EntityCondition<FieldConfiguration<ResourceLocation>> {

	public AdvancementCondition() {
		super(FieldConfiguration.codec(ResourceLocation.CODEC, "advancement"));
	}

	protected boolean testClient(FieldConfiguration<ResourceLocation> configuration, Entity entity) {
		return false;
	}

	@Override
	public boolean check(FieldConfiguration<ResourceLocation> configuration, Entity entity) {
		if (entity instanceof ServerPlayer) {
			Advancement advancement = Objects.requireNonNull(entity.getServer()).getAdvancements().getAdvancement(configuration.value());
			if (advancement == null)
				Apoli.LOGGER.warn("Advancement \"{}\" did not exist, but was referenced in an \"origins:advancement\" condition.", configuration.value().toString());
			else
				return ((ServerPlayer) entity).getAdvancements().getOrStartProgress(advancement).isDone();
		}
		return this.testClient(configuration, entity);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Client extends AdvancementCondition {
		public Client() {
			super();
		}

		@Override
		protected boolean testClient(FieldConfiguration<ResourceLocation> configuration, Entity entity) {
			if (entity instanceof LocalPlayer) {
				ClientAdvancements advancementManager = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getAdvancements();
				Advancement advancement = advancementManager.getAdvancements().get(configuration.value());
				if (advancement != null) {
					if (advancementManager.progress.containsKey(advancement))
						return advancementManager.progress.get(advancement).isDone();
				}
				// We don't want to print an error here if the advancement does not exist,
				// because on the client-side the advancement could just not have been received from the server.
			}
			return false;
		}
	}
}
