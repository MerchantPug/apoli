package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.power.factory.EntityAction;
import dev.experimental.apoli.common.action.configuration.ExperienceConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AddExperienceAction extends EntityAction<ExperienceConfiguration> {

	public AddExperienceAction() {
		super(ExperienceConfiguration.CODEC);
	}

	@Override
	public void execute(ExperienceConfiguration configuration, Entity entity) {

		if (entity instanceof PlayerEntity) {
			if (configuration.points() > 0)
				((PlayerEntity) entity).addExperience(configuration.points());
			((PlayerEntity) entity).addExperienceLevels(configuration.levels());
		}
	}
}
