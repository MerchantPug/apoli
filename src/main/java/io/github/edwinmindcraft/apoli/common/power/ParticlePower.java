package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.ParticleConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ModPowers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticlePower extends PowerFactory<ParticleConfiguration> {

	@OnlyIn(Dist.CLIENT)
	public static void renderParticles(LivingEntity player) {
		IPowerContainer.getPowers(player, ModPowers.PARTICLE.get()).stream().filter(x -> player.tickCount % x.getConfiguration().frequency() == 0)
				.forEach(power -> player.level.addParticle((ParticleOptions) power.getConfiguration().particle(), player.getRandomX(0.5), player.getRandomY(), player.getRandomZ(0.5), 0, 0, 0));
	}

	public ParticlePower() {
		super(ParticleConfiguration.CODEC);
	}
}
