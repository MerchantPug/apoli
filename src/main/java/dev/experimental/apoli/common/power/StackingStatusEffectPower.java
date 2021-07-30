package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.StackingStatusEffectConfiguration;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

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
	public Tag serialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		return IntTag.valueOf(this.getCurrentStacks(configuration, player).get());
	}

	@Override
	public void deserialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player, Tag tag) {
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
