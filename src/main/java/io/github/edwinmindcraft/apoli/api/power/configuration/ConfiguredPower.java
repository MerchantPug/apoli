package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.IHudRenderedPower;
import io.github.edwinmindcraft.apoli.api.power.IVariableIntPower;
import io.github.edwinmindcraft.apoli.api.power.PowerData;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliBuiltinRegistries;
import io.github.edwinmindcraft.calio.api.network.IContextAwareCodec;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IRegistryDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
public final class ConfiguredPower<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> extends CapabilityProvider<ConfiguredPower<?, ?>> implements IForgeRegistryEntry<ConfiguredPower<?, ?>>, IDynamicFeatureConfiguration {
	public static final Codec<ConfiguredPower<?, ?>> CODEC = PowerFactory.CODEC.dispatch(ConfiguredPower::getFactory, PowerFactory::getCodec);
	private final F factory;
	private final C configuration;
	private final PowerData data;

	public ConfiguredPower(F factory, C configuration, PowerData data) {
		super(ApoliBuiltinRegistries.CONFIGURED_POWER_CLASS);
		this.factory = factory;
		this.configuration = configuration;
		this.data = data;
		this.gatherCapabilities(this.factory::initCapabilities);
	}

	public PowerData getData() {
		return this.data;
	}

	public Map<String, ConfiguredPower<?, ?>> getContainedPowers() {
		return this.getFactory().getContainedPowers(this);
	}

	public boolean isActive(Entity entity) {
		return this.getConfiguration().isConfigurationValid() && this.getFactory().isActive(this, entity);
	}

	public void onGained(Entity entity) {
		this.getFactory().onGained(this, entity);
	}

	public void onRemoved(Entity entity) {
		this.getFactory().onRemoved(this, entity);
	}

	public void onLost(Entity entity) {
		this.getFactory().onLost(this, entity);
	}

	public void onAdded(Entity entity) {
		this.getFactory().onAdded(this, entity);
	}

	public void onRespawn(Entity entity) {
		this.getFactory().onRespawn(this, entity);
	}

	public <T> T getPowerData(Entity player, Supplier<? extends T> supplier) {
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

	public CompoundTag serialize(IPowerContainer container) {
		CompoundTag tag = new CompoundTag();
		CompoundTag caps = this.serializeCaps();
		if (caps != null && !caps.isEmpty()) tag.put("ForgeCaps", caps);
		this.getFactory().serialize(this, container, tag);
		return caps;
	}

	public void deserialize(IPowerContainer container, CompoundTag tag) {
		this.getFactory().deserialize(this, container, tag);
	}

	/**
	 * Executes a tick of the current factory if it is eligible to.<br/>
	 * You cannot force a tick of a non-ticking power.
	 *
	 * @param entity The entity to execute the action on.
	 * @param force  If true, there won't be any check to {@link PowerFactory#tickInterval(ConfiguredPower, Entity)}.
	 *
	 * @see #tick(Entity) for a version without the ability to be forced.
	 */
	public void tick(Entity entity, boolean force) {
		if (this.getFactory().canTick(this, entity)) {
			if (!force) {
				int i = this.getFactory().tickInterval(this, entity);
				if (i <= 0 || (entity.tickCount % i) != 0)
					return;
			}
			this.getFactory().tick(this, entity);
		}
	}

	public void tick(Entity entity) {
		this.tick(entity, false);
	}

	//VariableIntPower
	@SuppressWarnings("unchecked")
	public Optional<IVariableIntPower<C>> asVariableIntPower() {
		if (this.getFactory() instanceof IVariableIntPower<?> variableIntPower)
			return Optional.of((IVariableIntPower<C>) variableIntPower);
		return Optional.empty();
	}

	public OptionalInt assign(Entity entity, int value) {
		return this.asVariableIntPower().map(t -> t.assign(this, entity, value)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getValue(Entity entity) {
		return this.asVariableIntPower().map(t -> t.getValue(this, entity)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getMaximum(Entity entity) {
		return this.asVariableIntPower().map(t -> t.getMaximum(this, entity)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt getMinimum(Entity entity) {
		return this.asVariableIntPower().map(t -> t.getMinimum(this, entity)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt change(Entity entity, int amount) {
		return this.asVariableIntPower().map(t -> t.change(this, entity, amount)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt increment(Entity entity) {
		return this.asVariableIntPower().map(t -> t.increment(this, entity)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	public OptionalInt decrement(Entity entity) {
		return this.asVariableIntPower().map(t -> t.decrement(this, entity)).map(OptionalInt::of).orElseGet(OptionalInt::empty);
	}

	//Hud Renderered Power

	@SuppressWarnings("unchecked")
	public Optional<IHudRenderedPower<C>> asHudRendered() {
		return this.getFactory() instanceof IHudRenderedPower<?> hudRenderedPower ? Optional.of((IHudRenderedPower<C>) hudRenderedPower) : Optional.empty();
	}

	public Optional<HudRender> getRenderSettings(Entity entity) {
		return this.asHudRendered().map(x -> x.getRenderSettings(this, entity));
	}

	public Optional<Boolean> shouldRender(Entity entity) {
		return this.asHudRendered().map(x -> x.shouldRender(this, entity));
	}

	public Optional<Float> getFill(Entity entity) {
		return this.asHudRendered().map(x -> x.getFill(this, entity));
	}

	//Active Power

	@SuppressWarnings("unchecked")
	public Optional<IActivePower<C>> asActive() {
		return this.getFactory() instanceof IActivePower<?> hudRenderedPower ? Optional.of((IActivePower<C>) hudRenderedPower) : Optional.empty();
	}

	public boolean activate(Entity entity) {
		Optional<IActivePower<C>> ciActivePower = this.asActive();
		ciActivePower.ifPresent(x -> x.activate(this, entity));
		return ciActivePower.isPresent();
	}

	public Optional<IActivePower.Key> getKey(Entity entity) {
		return this.asActive().map(x -> x.getKey(this, entity));
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

	public F getFactory() {
		return this.factory;
	}

	public C getConfiguration() {
		return this.configuration;
	}

	@Override
	public @NotNull List<String> getErrors(@NotNull ICalioDynamicRegistryManager server) {
		return this.getConfiguration().getErrors(server);
	}

	@Override
	public @NotNull List<String> getWarnings(@NotNull ICalioDynamicRegistryManager server) {
		return this.getConfiguration().getWarnings(server);
	}

	@Override
	public boolean isConfigurationValid() {
		return this.getConfiguration().isConfigurationValid();
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
