package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.StackingStatusEffectConfiguration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;

import java.util.concurrent.atomic.AtomicInteger;

public class StackingStatusEffectPower extends PowerFactory<StackingStatusEffectConfiguration> {
	public StackingStatusEffectPower() {
		super(StackingStatusEffectConfiguration.CODEC);
		this.ticking(true);
	}

	public AtomicInteger getCurrentStacks(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new AtomicInteger(0));
	}

	@Override
	public void tick(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		AtomicInteger currentStacks = this.getCurrentStacks(configuration, player);
		StackingStatusEffectConfiguration config = configuration.getConfiguration();
		if (configuration.isActive(player)) {
			int i = currentStacks.addAndGet(1);
			if (i > config.max())
				currentStacks.set(config.max());
			if (i > 0)
				this.applyEffects(configuration, player);
		} else {
			int i = currentStacks.addAndGet(-1);
			if (i < config.min())
				currentStacks.set(config.min());
		}
	}

	@Override
	protected int tickInterval(StackingStatusEffectConfiguration configuration, LivingEntity player) {
		return 10;
	}

	@Override
	public NbtElement serialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		return NbtInt.of(this.getCurrentStacks(configuration, player).get());
	}

	@Override
	public void deserialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player, NbtElement tag) {
		if (tag instanceof NbtInt intTag)
			this.getCurrentStacks(configuration, player).set(intTag.intValue());
	}

	public void applyEffects(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		configuration.getConfiguration().effects().getContent().forEach(sei -> {
			int duration = configuration.getConfiguration().duration() * this.getCurrentStacks(configuration, player).get();
			if (duration > 0) {
				StatusEffectInstance applySei = new StatusEffectInstance(sei.getEffectType(), duration, sei.getAmplifier(), sei.isAmbient(), sei.shouldShowParticles(), sei.shouldShowIcon());
				player.addStatusEffect(applySei);
			}
		});
	}
}
