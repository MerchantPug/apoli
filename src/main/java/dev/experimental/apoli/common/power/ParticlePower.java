package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ParticleConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.LivingEntity;

public class ParticlePower extends PowerFactory<ParticleConfiguration> {

	@Environment(EnvType.CLIENT)
	public static void renderParticles(LivingEntity player) {
		IPowerContainer.getPowers(player, ModPowers.PARTICLE.get()).stream().filter(x -> player.age % x.getConfiguration().frequency() == 0)
				.forEach(power -> player.world.addParticle((ParticleEffect) power.getConfiguration().particle(), player.getParticleX(0.5), player.getRandomBodyY(), player.getParticleZ(0.5), 0, 0, 0));
	}

	public ParticlePower() {
		super(ParticleConfiguration.CODEC);
	}
}
