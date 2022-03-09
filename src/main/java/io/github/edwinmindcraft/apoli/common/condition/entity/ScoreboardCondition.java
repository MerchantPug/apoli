package io.github.edwinmindcraft.apoli.common.condition.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.ScoreboardComparisonConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardCondition extends EntityCondition<ScoreboardComparisonConfiguration> {

	public ScoreboardCondition() {
		super(ScoreboardComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(ScoreboardComparisonConfiguration configuration, Entity entity) {
		if (entity instanceof Player player) {
			Scoreboard scoreboard = player.getScoreboard();
			Objective objective = scoreboard.getOrCreateObjective(configuration.objective());
			String playerName = player.getName().getContents();

			if (scoreboard.hasPlayerScore(playerName, objective)) {
				int value = scoreboard.getOrCreatePlayerScore(playerName, objective).getScore();
				return configuration.comparison().check(value);
			}
		}
		return false;
	}
}
