package io.github.edwinmindcraft.apoli.fabric;

import com.mojang.serialization.Codec;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class FabricPowerFactory<P extends Power> extends PowerFactory<FabricPowerConfiguration<P>> {
	public FabricPowerFactory(Codec<FabricPowerConfiguration<P>> codec, boolean allowConditions) {
		super(codec, allowConditions);
	}

	@SuppressWarnings("unchecked")
	private P getPower(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		return configuration.getPowerData(entity, () -> configuration.getConfiguration().power().apply((PowerType<P>) configuration.getPowerType(), entity));
	}

	@SuppressWarnings("unchecked")
	private P getPower(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity, IPowerContainer container) {
		return configuration.getPowerData(container, () -> configuration.getConfiguration().power().apply((PowerType<P>) configuration.getPowerType(), entity));
	}

	@Override
	public boolean isActive(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		return super.isActive(configuration, entity) && this.getPower(configuration, entity).isActive();
	}

	@Override
	protected boolean shouldTickWhenActive(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		return this.getPower(configuration, entity).shouldTick();
	}

	@Override
	protected boolean shouldTickWhenInactive(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		return this.getPower(configuration, entity).shouldTickWhenInactive();
	}

	@Override
	public void tick(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).tick();
	}

	@Override
	public void onGained(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).onGained();
	}

	@Override
	public void onLost(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).onLost();
	}

	@Override
	public void onAdded(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).onAdded();
	}

	@Override
	public void onRemoved(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).onRemoved();
	}

	@Override
	public void onRespawn(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity) {
		this.getPower(configuration, entity).onRespawn();
	}

	@Override
	public Tag serialize(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity, IPowerContainer container) {
		return this.getPower(configuration, entity, container).toTag();
	}

	@Override
	public void deserialize(ConfiguredPower<FabricPowerConfiguration<P>, ?> configuration, LivingEntity entity, IPowerContainer container, Tag tag) {
		this.getPower(configuration, entity, container).fromTag(tag);
	}
}
