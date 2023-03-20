package io.github.edwinmindcraft.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.IAttributeModifyingPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IAttributeModifyingPowerConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class AttributeModifyingPowerFactory<T extends IAttributeModifyingPowerConfiguration> extends PowerFactory<T> implements IAttributeModifyingPower<T> {

	private final Lazy<Attribute> lazyAttribute;

	protected AttributeModifyingPowerFactory(Codec<T> codec) {
		this(codec, true);
	}

	protected AttributeModifyingPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
		this.lazyAttribute = Lazy.concurrentOf(this::getAttribute);
	}

	public boolean hasAttributeBacking() {
		return this.lazyAttribute.get() != null;
	}

	private Optional<AttributeInstance> getAttribute(Entity entity) {
		if (entity instanceof LivingEntity player)
			return player.getAttributes().hasAttribute(this.lazyAttribute.get()) ? Optional.ofNullable(player.getAttribute(this.lazyAttribute.get())) : Optional.empty();
		return Optional.empty();
	}

	protected void add(List<AttributeModifier> configuration, Entity player) {
		this.getAttribute(player).ifPresent(x -> configuration.stream().filter(mod -> !x.hasModifier(mod)).forEach(x::addTransientModifier));
	}

	protected void remove(List<AttributeModifier> configuration, Entity player) {
		this.getAttribute(player).ifPresent(x -> configuration.stream().filter(x::hasModifier).forEach(x::removeModifier));
	}

	@Override
	public boolean canTick(ConfiguredPower<T, ?> configuration, Entity entity) {
		return this.hasAttributeBacking() && this.shouldCheckConditions(configuration, entity);
	}

	@Override
	public void tick(ConfiguredPower<T, ?> configuration, Entity player) {
		if (this.canTick(configuration, player)) {
			if (configuration.isActive(player))
				this.add(configuration.getConfiguration().modifiers().getContent(), player);
			else
				this.remove(configuration.getConfiguration().modifiers().getContent(), player);
		}
	}

	@Override
	public void onAdded(ConfiguredPower<T, ?> configuration, Entity entity) {
		if (this.hasAttributeBacking() && !this.shouldCheckConditions(configuration, entity))
			this.add(configuration.getConfiguration().modifiers().getContent(), entity);
	}

	@Override
	protected void onRemoved(T configuration, Entity player) {
		this.remove(configuration.modifiers().getContent(), player);
	}

	@Override
	protected int tickInterval(T configuration, Entity player) {
		return 20;
	}

	@Override
	public List<AttributeModifier> getModifiers(ConfiguredPower<T, ?> configuration, Entity player) {
		return configuration.getConfiguration().modifiers().getContent();
	}

	@Nullable
	public abstract Attribute getAttribute();
}
