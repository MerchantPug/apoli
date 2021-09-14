package io.github.edwinmindcraft.apoli.common.action.entity;

import io.github.edwinmindcraft.apoli.common.action.configuration.FoodConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FeedAction extends EntityAction<FoodConfiguration> {

	public FeedAction() {
		super(FoodConfiguration.CODEC);
	}

	@Override
	public void execute(FoodConfiguration configuration, Entity entity) {
		if (entity instanceof Player player)
			player.getFoodData().eat(configuration.food(), configuration.saturation());
	}
}
