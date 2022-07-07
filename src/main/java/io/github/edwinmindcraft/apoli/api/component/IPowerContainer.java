package io.github.edwinmindcraft.apoli.api.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.integration.ModifyValueEvent;
import io.github.apace100.apoli.util.AttributeUtil;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.IValueModifyingPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.apoli.common.power.AttributeModifyTransferPower;
import io.github.edwinmindcraft.apoli.common.registry.ApoliCapabilities;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//FIXME Reintroduce PowerHolderComponent

/**
 * Represents a power container.<br>
 * Please note that this will only be available on forge unless specified otherwise.
 * If you want a cross compatible version, use PowerHolderComponent
 * instead.<br>
 * Please note that there is no warranty of mutability for any output, as such, please expect
 * every collection to be either immutable, or unmodifiable.
 */
public interface IPowerContainer {
	//region Static Methods
	ResourceLocation KEY = Apoli.identifier("powers");

	@NotNull
	static LazyOptional<IPowerContainer> get(@Nullable Entity entity) {
		return entity == null ? LazyOptional.empty() : entity.getCapability(ApoliCapabilities.POWER_CONTAINER);
	}

	static void sync(Entity entity) {
		ApoliAPI.synchronizePowerContainer(entity);
	}

	static void sync(Entity entity, ServerPlayer with) {
		ApoliAPI.synchronizePowerContainer(entity, with);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> void withPower(Entity entity, F factory, @Nullable Predicate<Holder<ConfiguredPower<T, F>>> power, Consumer<Holder<ConfiguredPower<T, F>>> with) {
		get(entity).ifPresent(x -> x.getPowers(factory).stream().filter(Holder::isBound).filter(p -> power == null || power.test(p)).findAny().ifPresent(with));
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> List<Holder<ConfiguredPower<T, F>>> getPowers(Entity entity, F factory) {
		return get(entity).map(x -> x.getPowers(factory)).orElseGet(ImmutableList::of);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> boolean hasPower(Entity entity, F factory) {
		return get(entity).map(x -> x.getPowers().stream().anyMatch(p -> p.isBound() && Objects.equals(factory, p.value().getFactory()) && p.value().isActive(entity))).orElse(false);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue) {
		return (float) modify(entity, factory, (double) baseValue, null, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue, Predicate<Holder<ConfiguredPower<T, F>>> powerFilter) {
		return (float) modify(entity, factory, (double) baseValue, powerFilter, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue, Predicate<Holder<ConfiguredPower<T, F>>> powerFilter, Consumer<Holder<ConfiguredPower<T, F>>> powerAction) {
		return (float) modify(entity, factory, (double) baseValue, powerFilter, powerAction);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> double modify(Entity entity, F factory, double baseValue) {
		return modify(entity, factory, baseValue, null, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> double modify(Entity entity, F factory, double baseValue, Predicate<Holder<ConfiguredPower<T, F>>> powerFilter, Consumer<Holder<ConfiguredPower<T, F>>> powerAction) {
		List<Holder<ConfiguredPower<T, F>>> powers = IPowerContainer.getPowers(entity, factory).stream().filter(x -> powerFilter == null || powerFilter.test(x)).toList();
		List<AttributeModifier> modifiers = powers.stream().filter(Holder::isBound).flatMap(x -> x.value().getFactory().getModifiers(x.value(), entity).stream()).collect(Collectors.toCollection(ArrayList::new));
		if (powerAction != null) powers.forEach(powerAction);
		modifiers.addAll(AttributeModifyTransferPower.apply(entity, factory));
		ModifyValueEvent event = new ModifyValueEvent(entity, factory, baseValue, modifiers);
		MinecraftForge.EVENT_BUS.post(event);
		return AttributeUtil.applyModifiers(event.getModifiers(), baseValue);
	}

	//endregion

	/**
	 * Removes the given power if it is the only instance for the given source.
	 *
	 * @param power  The power to remove.
	 * @param source The source of this power.
	 */
	@Contract(mutates = "this")
	default void removePower(ResourceLocation power, ResourceLocation source) {
		this.removePower(ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power), source);
	}

	/**
	 * Removes the given power if it is the only instance for the given source.
	 *
	 * @param power  The power to remove.
	 * @param source The source of this power.
	 */
	@Contract(mutates = "this")
	void removePower(ResourceKey<ConfiguredPower<?, ?>> power, ResourceLocation source);

	/**
	 * Removes all powers associated with the given source.
	 *
	 * @param source The source of the powers.
	 *
	 * @return The amount of powers that were removed.
	 */
	@Contract(mutates = "this")
	int removeAllPowersFromSource(ResourceLocation source);

	/**
	 * Returns a list the names of all powers for the given source.
	 *
	 * @param source The source of the powers.
	 *
	 * @return A {@link List} of powers.
	 */
	@NotNull
	List<ResourceKey<ConfiguredPower<?, ?>>> getPowersFromSource(ResourceLocation source);

	/**
	 * Adds the given power with the given source.
	 *
	 * @param power  The power to add.
	 * @param source The source of this power.
	 *
	 * @return {@code true} if the power was added, {@code false} if it was already present.
	 */
	@Contract(mutates = "this")
	default boolean addPower(ResourceLocation power, ResourceLocation source) {
		return this.addPower(ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power), source);
	}

	/**
	 * Adds the given power with the given source.
	 *
	 * @param power  The power to add.
	 * @param source The source of this power.
	 *
	 * @return {@code true} if the power was added, {@code false} if it was already present.
	 */
	@Contract(mutates = "this")
	boolean addPower(ResourceKey<ConfiguredPower<?, ?>> power, ResourceLocation source);

	/**
	 * Checks if the entity has the given power.
	 *
	 * @param power The power to check for.
	 *
	 * @return {@code true} if the player has the power, {@code false} otherwise
	 */
	@Contract(pure = true)
	default boolean hasPower(@Nullable ResourceLocation power) {
		return this.hasPower(power == null ? null : ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power));
	}

	/**
	 * Checks if the entity has the given power.
	 *
	 * @param power The power to check for.
	 *
	 * @return {@code true} if the player has the power, {@code false} otherwise
	 */
	@Contract(pure = true)
	boolean hasPower(@Nullable ResourceKey<ConfiguredPower<?, ?>> power);

	/**
	 * Checks if the given source gives the requested power.
	 *
	 * @param power  The power to check for.
	 * @param source The requested source of this power.
	 *
	 * @return {@code true} if the player has the power with the given source, {@code false} otherwise
	 */
	@Contract(pure = true)
	default boolean hasPower(ResourceLocation power, ResourceLocation source) {
		return this.hasPower(ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power), source);
	}

	/**
	 * Checks if the given source gives the requested power.
	 *
	 * @param power  The power to check for.
	 * @param source The requested source of this power.
	 *
	 * @return {@code true} if the player has the power with the given source, {@code false} otherwise
	 */
	@Contract(pure = true)
	boolean hasPower(ResourceKey<ConfiguredPower<?, ?>> power, ResourceLocation source);

	/**
	 * Returns the power with the given name if the player has this power, false otherwise.
	 *
	 * @param power The {@link ResourceLocation} of the power to get.
	 * @param <C>   The type of the {@link IDynamicFeatureConfiguration} of this power.
	 * @param <F>   The type of the {@link PowerFactory} of this power.
	 *
	 * @return The {@link ConfiguredPower} with the given name if this entity has it, {@code null} otherwise.
	 */
	@Nullable
	@Contract(value = "null -> null", pure = true)
	default <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> Holder<ConfiguredPower<C, F>> getPower(@Nullable ResourceLocation power) {
		if (power == null)
			return null;
		return this.getPower(ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power));
	}

	/**
	 * Returns the power with the given name if the player has this power, false otherwise.
	 *
	 * @param power The {@link ResourceLocation} of the power to get.
	 * @param <C>   The type of the {@link IDynamicFeatureConfiguration} of this power.
	 * @param <F>   The type of the {@link PowerFactory} of this power.
	 *
	 * @return The {@link ConfiguredPower} with the given name if this entity has it, {@code null} otherwise.
	 */
	@Nullable
	@Contract(value = "null -> null", pure = true)
	<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> Holder<ConfiguredPower<C, F>> getPower(@Nullable ResourceKey<ConfiguredPower<?, ?>> power);

	/**
	 * Returns a list of all powers currently on this entity.
	 * This includes both active and inactive powers.
	 *
	 * @return A {@link List} of the instances of {@link ConfiguredPower} currently on this component
	 */
	@NotNull
	@Contract(pure = true)
	List<Holder<ConfiguredPower<?, ?>>> getPowers();

	/**
	 * Returns a list of the names of all powers currently on this entity.
	 * This includes both active and inactive powers.
	 *
	 * @return A {@link Set} of the names of all the instances of {@link ConfiguredPower} currently on this component
	 */
	@NotNull
	@Contract(pure = true)
	default Set<ResourceKey<ConfiguredPower<?, ?>>> getPowerNames() {
		return this.getPowerTypes(true);
	}

	/**
	 * Returns a set of Identifiers containing all power currently contained in this component,
	 * with a possibility to exclude sub-powers.
	 *
	 * @param includeSubPowers if this is true, all powers, including lower level ones will be included.
	 *                         Otherwise, only the top level powers will be included
	 *
	 * @return A {@link Set} containing the names of all powers currently on this entity.
	 */
	@NotNull
	@Contract(pure = true)
	Set<ResourceKey<ConfiguredPower<?, ?>>> getPowerTypes(boolean includeSubPowers);

	/**
	 * Returns a list of all active powers on this component of the given type.
	 *
	 * @param factory The type of power to get.
	 * @param <C>     The type of the {@link IDynamicFeatureConfiguration} of the powers.
	 * @param <F>     The type of the {@link PowerFactory} of the powers.
	 *
	 * @return A {@link List} containing all {@link ConfiguredPower} of the given type currently active on this entity.
	 *
	 * @see #getPowers(PowerFactory, boolean) For a version that allows inactive powers to be accessed.
	 */
	@NotNull
	@Contract(pure = true)
	default <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> List<Holder<ConfiguredPower<C, F>>> getPowers(F factory) {
		return this.getPowers(factory, false);
	}

	/**
	 * Returns a list of all powers on this component of the given type.
	 *
	 * @param factory         The type of power to get.
	 * @param includeInactive If {@code true}, inactive powers will be included in the returned set.
	 * @param <C>             The type of the {@link IDynamicFeatureConfiguration} of the powers.
	 * @param <F>             The type of the {@link PowerFactory} of the powers.
	 *
	 * @return A {@link List} containing all {@link ConfiguredPower} of the given type currently on this entity.
	 *
	 * @see #getPowers(PowerFactory) For a version that get all active powers.
	 */
	@NotNull
	@Contract(pure = true)
	<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> List<Holder<ConfiguredPower<C, F>>> getPowers(F factory, boolean includeInactive);

	/**
	 * Returns a list of all sources for the given power.
	 *
	 * @param power The power to get the source for.
	 *
	 * @return A {@link List} of all sources of this power.
	 */
	@NotNull
	@Contract(pure = true)
	default List<ResourceLocation> getSources(ResourceLocation power) {
		return this.getSources(ResourceKey.create(ApoliDynamicRegistries.CONFIGURED_POWER_KEY, power));
	}

	/**
	 * Returns a list of all sources for the given power.
	 *
	 * @param power The power to get the source for.
	 *
	 * @return A {@link List} of all sources of this power.
	 */
	@NotNull
	@Contract(pure = true)
	List<ResourceLocation> getSources(ResourceKey<ConfiguredPower<?, ?>> power);

	/**
	 * Synchronizes this component with the client.
	 */
	@Contract(pure = true)
	void sync();

	/**
	 * Executes a tick on the server.
	 */
	@Contract(mutates = "this")
	void serverTick();

	/**
	 * Reads the data from this component to from the given tag.
	 *
	 * @param tag The input tag.
	 */
	@Contract(mutates = "this")
	default void readFromNbt(CompoundTag tag) {
		this.readNbt(tag, true);
	}

	/**
	 * Reads the data from this component to from the given tag.
	 *
	 * @param tag         The input tag.
	 * @param applyEvents Whether to apply {@link ConfiguredPower#onRemoved(Entity)}, {@link ConfiguredPower#onLost(Entity)},
	 *                    {@link ConfiguredPower#onAdded(Entity)} and {@link ConfiguredPower#onGained(Entity)} or not.
	 */
	@Contract(mutates = "this")
	void readNbt(CompoundTag tag, boolean applyEvents);

	void rebuildCache();

	/**
	 * Replaces the currently held data by the data provided
	 *
	 * @param powerSources A multimap containing all powers and their sources.
	 * @param data         A map that may contain the power data if applicable.
	 */
	@Contract(mutates = "this")
	void handle(Multimap<ResourceLocation, ResourceLocation> powerSources, Map<ResourceLocation, CompoundTag> data);

	/**
	 * Writes this component to the given tag.
	 *
	 * @param tag The tag to write the data on.
	 */
	@Contract(mutates = "param")
	CompoundTag writeToNbt(CompoundTag tag);

	/**
	 * This is a replacement for the system used by fabric in which data is stored with the powers.<br/>
	 * This expects the type to be mutable, as it will only be computed if it is missing.
	 *
	 * @param power    The power to create the data for.
	 * @param supplier The default value of the entry
	 * @param <T>      The type of the stored data. Should be mutable, you can use Atomic types.
	 *
	 * @return Either the stored data for this power, or a new instance of the data.
	 */
	@NotNull <T> T getPowerData(ResourceKey<ConfiguredPower<?, ?>> power, NonNullSupplier<? extends T> supplier);

	/**
	 * This is a replacement for the system used by fabric in which data is stored with the powers.<br/>
	 * This expects the type to be mutable, as it will only be computed if it is missing.
	 *
	 * @param power    The power to create the data for.
	 * @param supplier The default value of the entry
	 * @param <T>      The type of the stored data. Should be mutable, you can use Atomic types.
	 *
	 * @return Either the stored data for this power, or a new instance of the data.
	 */
	default @NotNull <T> T getPowerData(Holder<ConfiguredPower<?, ?>> power, NonNullSupplier<? extends T> supplier) {
		ResourceKey<ConfiguredPower<?, ?>> key = power.unwrap()
				.map(Optional::of, pow -> ApoliAPI.getPowers(this.getOwner().getServer()).getResourceKey(pow))
				.orElseThrow(() -> new IllegalArgumentException("Cannot create data for unregistered power: " + power));
		return this.getPowerData(key, supplier);
	}

	Entity getOwner();
}
