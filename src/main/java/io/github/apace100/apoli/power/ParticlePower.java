package io.github.apace100.apoli.power;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;

public class ParticlePower extends Power {

    private final ParticleOptions particleEffect;
    private final int frequency;

    public ParticlePower(PowerType<?> type, LivingEntity entity, ParticleOptions particle, int frequency) {
        super(type, entity);
        this.particleEffect = particle;
        this.frequency = frequency;
    }

    public ParticleOptions getParticle() {
        return particleEffect;
    }

    public int getFrequency() {
        return frequency;
    }
}
