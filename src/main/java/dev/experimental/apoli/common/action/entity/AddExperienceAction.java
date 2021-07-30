package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.ExperienceConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AddExperienceAction extends EntityAction<ExperienceConfiguration> {

	public AddExperienceAction() {
		super(ExperienceConfiguration.CODEC);
	}

	@Override
	public void execute(ExperienceConfiguration configuration, Entity entity) {

		if (entity instanceof Player) {
			if (configuration.points() > 0)
				((Player) entity).giveExperiencePoints(configuration.points());
			((Player) entity).giveExperienceLevels(configuration.levels());
		}
	}
}
