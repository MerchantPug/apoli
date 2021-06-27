package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.FoodConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class FeedAction extends EntityAction<FoodConfiguration> {

	public FeedAction() {
		super(FoodConfiguration.CODEC);
	}

	@Override
	public void execute(FoodConfiguration configuration, Entity entity) {
		if (entity instanceof PlayerEntity player)
			player.getHungerManager().add(configuration.food(), configuration.saturation());
	}
}
