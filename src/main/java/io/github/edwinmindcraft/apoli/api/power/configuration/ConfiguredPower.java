package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.*;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliBuiltinRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IRegistryDelegate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
public final class ConfiguredPower<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> extends ConfiguredFactory<C, F> implements IForgeRegistryEntry<ConfiguredPower<?, ?>> {
	public static final Codec<ConfiguredPower<?, ?>> CODEC = PowerFactory.CODEC.dispatch(ConfiguredPower::getFactory, PowerFactory::getCodec);
	private final PowerData data;

	public ConfiguredPower(F factory, C configuration, PowerData data) {
		super(factory, configuration);
		this.data = data;
	}

	public PowerData getData() {
		return this.data;
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

	public <T> T getPowerData(LivingEntity player, Supplier<? extends T> supplier) {
		return IPowerContainer.get(player).resolve().map(x -> x.getPowerData(this, supplier)).orElse(null);
	}

	public <T> T getPowerData(IPowerContainer container, Supplier<? extends T> supplier) {
		return container.getPowerData(this, supplier);
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

	public Tag serialize(LivingEntity player, IPowerContainer container) {
		return this.getFactory().serialize(this, player, container);
	}

	public void deserialize(LivingEntity player, IPowerContainer container, Tag tag) {
		this.getFactory().deserialize(this, player, container, tag);
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

	@Override
	public Class<ConfiguredPower<?, ?>> getRegistryType() {
		return ApoliBuiltinRegistries.CONFIGURED_POWER_CLASS;
	}

	public final Delegate<ConfiguredPower<?, ?>> delegate = new Delegate<>(this, ApoliBuiltinRegistries.CONFIGURED_POWER_CLASS);
	private ResourceLocation registryName = null;

	public ConfiguredPower<?, ?> setRegistryName(String name) {
		if (this.getRegistryName() != null)
			throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + this.getRegistryName());

		this.registryName = this.checkRegistryName(name);
		this.delegate.setName(this.registryName);
		this.getContainedPowers().forEach((s, configuredPower) -> {
			if (configuredPower.getRegistryName() == null) configuredPower.setRegistryName(name + s);
		});
		return this;
	}

	//Helper functions
	@Override
	public ConfiguredPower<?, ?> setRegistryName(ResourceLocation name) {return this.setRegistryName(name.toString());}

	public ConfiguredPower<?, ?> setRegistryName(String modID, String name) {return this.setRegistryName(modID + ":" + name);}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		if (this.delegate.name() != null) return this.delegate.name();
		return this.registryName != null ? this.registryName : null;
	}

	public ConfiguredPower<C, F> complete(ResourceLocation name) {
		return new ConfiguredPower<>(this.getFactory(), this.getConfiguration(), this.getData().complete(name));
	}


	/**
	 * This will assert that the registry name is valid and warn about potential registry overrides
	 * It is important as it detects cases where modders unintentionally register objects with the "minecraft" namespace, leading to dangerous errors later.
	 *
	 * @param name The registry name
	 *
	 * @return A verified "correct" registry name
	 */
	ResourceLocation checkRegistryName(String name) {
		return new ResourceLocation(name);
	}

	@Override
	public String toString() {
		return "CP:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}

	private final Lazy<PowerType<?>> type = Lazy.of(() -> new PowerType<>(this));

	public PowerType<?> getPowerType() {
		return this.type.get();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		ConfiguredPower<?, ?> that = (ConfiguredPower<?, ?>) o;
		return this.delegate.equals(that.delegate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.delegate);
	}

	private static final class Delegate<T> implements IRegistryDelegate<T> {
		private T referent;
		private ResourceLocation name;
		private final Class<T> type;

		public Delegate(T referent, Class<T> type) {
			this.referent = referent;
			this.type = type;
		}

		@Override
		public T get() {return this.referent;}

		@Override
		public ResourceLocation name() {return this.name;}

		@Override
		public Class<T> type() {return this.type;}

		void changeReference(T newTarget) {this.referent = newTarget;}

		public void setName(ResourceLocation name) {this.name = name;}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Delegate<?> other) {
				return Objects.equals(other.name, this.name);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(this.name);
		}
	}
}
