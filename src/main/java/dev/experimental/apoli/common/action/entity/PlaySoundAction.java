package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.common.action.configuration.PlaySoundConfiguration;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import dev.experimental.apoli.api.power.factory.EntityAction;

public class PlaySoundAction extends EntityAction<PlaySoundConfiguration> {

	public PlaySoundAction() {
		super(PlaySoundConfiguration.CODEC);
	}

	@Override
	public void execute(PlaySoundConfiguration configuration, Entity entity) {
		if (entity instanceof Player && configuration.sound() != null)
			entity.level.playSound(null, (entity).getX(), (entity).getY(), (entity).getZ(), configuration.sound(), SoundSource.PLAYERS, configuration.volume(), configuration.pitch());
	}
}
