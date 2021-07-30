package dev.experimental.apoli.common.condition.entity;

import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.common.condition.configuration.ScoreboardComparisonConfiguration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardCondition extends EntityCondition<ScoreboardComparisonConfiguration> {

	public ScoreboardCondition() {
		super(ScoreboardComparisonConfiguration.CODEC);
	}

	@Override
	public boolean check(ScoreboardComparisonConfiguration configuration, LivingEntity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
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
