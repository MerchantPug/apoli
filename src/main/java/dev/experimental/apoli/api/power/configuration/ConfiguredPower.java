package dev.experimental.apoli.api.power.configuration;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.*;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is a replacement for the power system used by fabric.
 * It should be better when it comes to memory usage since every power is only instantiated once.
 * The main drawback is when it comes to mutable variables, in which case this system feels wrong
 * to use, although consider using provided types, as they (mostly) hide all this in simple cases.
 *
 * @param <C> The type of the configuration.
 * @param <F> The type of the factory.
 */
public final class ConfiguredPower<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredPower<?, ?>> CODEC = PowerFactory.CODEC.dispatch(ConfiguredPower::getFactory, Function.identity());
	private final PowerData data;

	public ConfiguredPower(F factory, C configuration, PowerData data) {
		super(factory, configuration);
		this.data = data;
	}

	public PowerData getData() {
		return data;
	}

	public Map<String, ConfiguredPower<?, ?>> getContainedPowers() {
		return this.getFactory().getContainedPowers(this);
	}

	public boolean isActive(LivingEntity player) {
		return this.getConfiguration().isConfigurationValid() && this.getFactory().isActive(this, player);
	}

	public void onGained(LivingEntity player) {
		this.getFactory().onGained(this, player);
	}

	public void onRemoved(LivingEntity player) {
		this.getFactory().onRemoved(this, player);
	}

	public void onLost(LivingEntity player) {
		this.getFactory().onLost(this, player);
	}

	public void onAdded(LivingEntity player) {
		this.getFactory().onAdded(this, player);
	}

	public void onRespawn(LivingEntity player) {
		this.getFactory().onRespawn(this, player);
	}

	public <C> C getPowerData(LivingEntity player, Supplier<? extends C> supplier) {
		return ApoliAPI.getPowerContainer(player).getPowerData(this, supplier);
	}

	/**
	 * Accesses a list of all children, recursively.<br>
	 * This allows for nested multiple powers, although this isn't a feature
	 * of the fabric version, as such I would recommend using it if you want
	 * to maintain compatibility between versions.
	 */
	public Set<ConfiguredPower<?, ?>> getChildren() {
		ImmutableSet.Builder<ConfiguredPower<?, ?>> builder = ImmutableSet.builder();
		this.getContainedPowers().values().forEach(value -> builder.add(value).addAll(value.getChildren()));
		return builder.build();
	}

	public Tag serialize(LivingEntity player) {
		return this.getFactory().serialize(this, player);
	}

	public void deserialize(LivingEntity player, Tag tag) {
		this.getFactory().deserialize(this, player, tag);
	}

	/**
	 * Executes a tick of the current factory if it is eligible to.<br/>
	 * You cannot force a tick of a non-ticking power.
	 *
	 * @param player The player to execute the action on.
	 * @param force  If true, there won't be any check to {@link PowerFactory#tickInterval(ConfiguredPower, LivingEntity)}.
	 *
	 * @see #tick(LivingEntity) for a version without the ability to be forced.
	 */
	public void tick(LivingEntity player, boolean force) {
		if (this.getFactory().canTick(this, player)) {
			if (!force) {
				int i = this.getFactory().tickInterval(this, player);
				if (i <= 0 || (player.tickCount % i) != 0)
					return;
			}
			this.getFactory().tick(this, player);
		}
	}

	public void tick(LivingEntity player) {
		this.tick(player, false);
	}

	//VariableIntPower
	@SuppressWarnings("unchecked")
	public Optional<IVariableIntPower<C>> asVariableIntPower() {
		if (this.getFactory() instanceof IVariableIntPower<?> variableIntPower)
			return Optional.of((IVariableIntPower<C>) variableIntPower);
		return Optional.empty();
	}

	public OptionalInt assign(LivingEntity player, int value) {
		return this.asVariableIntPower().map(t -> t.assign(this, player, value)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getValue(LivingEntity player) {
		return this.asVariableIntPower().map(t -> t.getValue(this, player)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getMaximum(LivingEntity player) {
		return this.asVariableIntPower().map(t -> t.getMaximum(this, player)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getMinimum(LivingEntity player) {
		return this.asVariableIntPower().map(t -> t.getMinimum(this, player)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt change(LivingEntity player, int amount) {
		return this.asVariableIntPower().map(t -> t.change(this, player, amount)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt increment(LivingEntity player) {
		return this.asVariableIntPower().map(t -> t.increment(this, player)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt decrement(LivingEntity player) {
		return this.asVariableIntPower().map(t -> t.decrement(this, player)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	//Hud Renderered Power

	@SuppressWarnings("unchecked")
	public Optional<IHudRenderedPower<C>> asHudRendered() {
		return this.getFactory() instanceof IHudRenderedPower<?> hudRenderedPower ? Optional.of((IHudRenderedPower<C>) hudRenderedPower) : Optional.empty();
	}

	public Optional<HudRender> getRenderSettings(LivingEntity player) {
		return this.asHudRendered().map(x -> x.getRenderSettings(this, player));
	}

	public Optional<Boolean> shouldRender(LivingEntity player) {
		return this.asHudRendered().map(x -> x.shouldRender(this, player));
	}

	public Optional<Float> getFill(LivingEntity player) {
		return this.asHudRendered().map(x -> x.getFill(this, player));
	}

	//Active Power

	@SuppressWarnings("unchecked")
	public Optional<IActivePower<C>> asActive() {
		return this.getFactory() instanceof IActivePower<?> hudRenderedPower ? Optional.of((IActivePower<C>) hudRenderedPower) : Optional.empty();
	}

	public boolean activate(LivingEntity player) {
		Optional<IActivePower<C>> ciActivePower = this.asActive();
		ciActivePower.ifPresent(x -> x.activate(this, player));
		return ciActivePower.isPresent();
	}

	public Optional<IActivePower.Key> getKey(LivingEntity player) {
		return this.asActive().map(x -> x.getKey(this, player));
	}
}
