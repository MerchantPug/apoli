package dev.experimental.apoli.api.power.factory.power;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class AttributeModifyingPowerFactory<T extends IValueModifyingPowerConfiguration> extends ValueModifyingPowerFactory<T> {
	public static <C extends IValueModifyingPowerConfiguration, F extends AttributeModifyingPowerFactory<C>> double apply(Entity entity, F type, double baseValue) {
		if (type.hasAttributeBacking()) return baseValue;
		return IPowerContainer.modify(entity, type, baseValue);
	}

	private final Lazy<EntityAttribute> lazyAttribute;

	protected AttributeModifyingPowerFactory(Codec<T> codec) {
		this(codec, true);
	}

	protected AttributeModifyingPowerFactory(Codec<T> codec, boolean allowConditions) {
		super(codec, allowConditions);
		this.lazyAttribute = new Lazy<>(this::getAttribute);
	}

	public boolean hasAttributeBacking() {
		return this.lazyAttribute.get() != null;
	}

	private Optional<EntityAttributeInstance> getAttribute(LivingEntity player) {
		return player.getAttributes().hasAttribute(this.lazyAttribute.get()) ? Optional.ofNullable(player.getAttributeInstance(this.lazyAttribute.get())) : Optional.empty();
	}

	private void add(List<EntityAttributeModifier> configuration, LivingEntity player) {
		this.getAttribute(player).ifPresent(x -> configuration.stream().filter(mod -> !x.hasModifier(mod)).forEach(x::addTemporaryModifier));
	}

	private void remove(List<EntityAttributeModifier> configuration, LivingEntity player) {
		this.getAttribute(player).ifPresent(x -> configuration.stream().filter(x::hasModifier).forEach(x::removeModifier));
	}

	@Override
	public boolean canTick(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.hasAttributeBacking() && this.shouldCheckConditions();
	}

	@Override
	public void tick(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		if (this.canTick(configuration, player)) {
			if (configuration.isActive(player))
				this.add(configuration.getConfiguration().modifiers().getContent(), player);
			else
				this.remove(configuration.getConfiguration().modifiers().getContent(), player);
		}
	}

	@Override
	protected void onAdded(T configuration, LivingEntity player) {
		if (this.hasAttributeBacking() && !this.shouldCheckConditions())
			this.add(configuration.modifiers().getContent(), player);
	}

	@Override
	protected void onRemoved(T configuration, LivingEntity player) {
		this.remove(configuration.modifiers().getContent(), player);
	}

	@Override
	protected int tickInterval(T configuration, LivingEntity player) {
		return 20;
	}

	@Nullable
	public abstract EntityAttribute getAttribute();
}
