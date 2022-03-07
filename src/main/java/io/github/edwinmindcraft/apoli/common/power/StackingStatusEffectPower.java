package io.github.edwinmindcraft.apoli.common.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.configuration.StackingStatusEffectConfiguration;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class StackingStatusEffectPower extends PowerFactory<StackingStatusEffectConfiguration> {
	public StackingStatusEffectPower() {
		super(StackingStatusEffectConfiguration.CODEC);
		this.ticking(true);
	}

	public AtomicInteger getCurrentStacks(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		return configuration.getPowerData(player, () -> new AtomicInteger(0));
	}

	public AtomicInteger getCurrentStacks(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, IPowerContainer player) {
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
	public Tag serialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player, IPowerContainer container) {
		return IntTag.valueOf(this.getCurrentStacks(configuration, container).get());
	}

	@Override
	public void deserialize(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player, IPowerContainer container, Tag tag) {
		if (tag instanceof IntTag intTag)
			this.getCurrentStacks(configuration, container).set(intTag.getAsInt());
	}

	public void applyEffects(ConfiguredPower<StackingStatusEffectConfiguration, ?> configuration, LivingEntity player) {
		configuration.getConfiguration().effects().getContent().forEach(sei -> {
			int duration = configuration.getConfiguration().duration() * this.getCurrentStacks(configuration, player).get();
			if (duration > 0) {
				MobEffectInstance applySei = new MobEffectInstance(sei.getEffect(), duration, sei.getAmplifier(), sei.isAmbient(), sei.isVisible(), sei.showIcon());
				player.addEffect(applySei);
			}
		});
	}
}
