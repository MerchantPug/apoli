package dev.experimental.apoli.api.component;

import com.google.common.collect.ImmutableList;
import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.IValueModifyingPower;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.apoli.util.AttributeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a power container.<br>
 * Please note that this will only be available on forge unless specified otherwise.
 * If you want a cross compatible version, use {@link io.github.apace100.apoli.component.PowerHolderComponent}
 * instead.<br>
 * Please note that there is no warranty of mutability for any output, as such, please expect
 * every collection to be either immutable, or unmodifiable.
 */
public interface IPowerContainer {
	//region Static Methods

	static void sync(LivingEntity living) {
		ApoliAPI.synchronizePowerContainer(living);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> void withPower(Entity entity, F factory, Predicate<ConfiguredPower<T, F>> power, Consumer<ConfiguredPower<T, F>> with) {
		if (entity instanceof LivingEntity)
			ApoliAPI.getPowerContainer(entity).getPowers(factory).stream().filter(p -> power == null || power.test(p)).findAny().ifPresent(with);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> List<ConfiguredPower<T, F>> getPowers(Entity entity, F factory) {
		if (entity instanceof LivingEntity) {
			return ApoliAPI.getPowerContainer(entity).getPowers(factory);
		}
		return ImmutableList.of();
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> boolean hasPower(Entity entity, F factory) {
		if (entity instanceof LivingEntity player) {
			return ApoliAPI.getPowerContainer(entity).getPowers().stream().anyMatch(p -> Objects.equals(factory, p.getFactory()) && p.isActive(player));
		}
		return false;
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue) {
		return (float) modify(entity, factory, (double) baseValue, null, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue, Predicate<ConfiguredPower<T, F>> powerFilter) {
		return (float) modify(entity, factory, (double) baseValue, powerFilter, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> float modify(Entity entity, F factory, float baseValue, Predicate<ConfiguredPower<T, F>> powerFilter, Consumer<ConfiguredPower<T, F>> powerAction) {
		return (float) modify(entity, factory, (double) baseValue, powerFilter, powerAction);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> double modify(Entity entity, F factory, double baseValue) {
		return modify(entity, factory, baseValue, null, null);
	}

	static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T> & IValueModifyingPower<T>> double modify(Entity entity, F factory, double baseValue, Predicate<ConfiguredPower<T, F>> powerFilter, Consumer<ConfiguredPower<T, F>> powerAction) {
		if (entity instanceof PlayerEntity player) {
			List<ConfiguredPower<T, F>> powers = IPowerContainer.getPowers(player, factory).stream().filter(x -> powerFilter == null || powerFilter.test(x)).toList();
			List<EntityAttributeModifier> modifiers = powers.stream().flatMap(x -> x.getFactory().getModifiers(x, player).stream()).toList();
			if (powerAction != null) powers.forEach(powerAction);
			return AttributeUtil.applyModifiers(modifiers, baseValue);
		}
		return baseValue;
	}

	//endregion

	/**
	 * Removes the given power if it is the only instance for the given source.
	 *
	 * @param power  The power to remove.
	 * @param source The source of this power.
	 */
	@Contract(mutates = "this")
	void removePower(Identifier power, Identifier source);

	/**
	 * Removes all powers associated with the given source.
	 *
	 * @param source The source of the powers.
	 *
	 * @return The amount of powers that were removed.
	 */
	@Contract(mutates = "this")
	int removeAllPowersFromSource(Identifier source);

	/**
	 * Returns a list the names of all powers for the given source.
	 *
	 * @param source The source of the powers.
	 *
	 * @return A {@link List} of powers.
	 */
	@NotNull
	List<Identifier> getPowersFromSource(Identifier source);

	/**
	 * Adds the given power with the given source.
	 *
	 * @param power  The power to add.
	 * @param source The source of this power.
	 *
	 * @return {@code true} if the power was added, {@code false} if it was already present.
	 */
	@Contract(mutates = "this")
	boolean addPower(Identifier power, Identifier source);

	/**
	 * Checks if the entity has the given power.
	 *
	 * @param power The power to check for.
	 *
	 * @return {@code true} if the player has the power, {@code false} otherwise
	 */
	@Contract(pure = true)
	boolean hasPower(Identifier power);

	/**
	 * Checks if the given source gives the requested power.
	 *
	 * @param power  The power to check for.
	 * @param source The requested source of this power.
	 *
	 * @return {@code true} if the player has the power with the given source, {@code false} otherwise
	 */
	@Contract(pure = true)
	boolean hasPower(Identifier power, Identifier source);

	/**
	 * Returns the power with the given name if the player has this power, false otherwise.
	 *
	 * @param power The {@link Identifier} of the power to get.
	 * @param <C>   The type of the {@link IDynamicFeatureConfiguration} of this power.
	 * @param <F>   The type of the {@link PowerFactory} of this power.
	 *
	 * @return The {@link ConfiguredPower} with the given name if this entity has it, {@code null} otherwise.
	 */
	@Nullable
	@Contract(value = "null -> null", pure = true)
	<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> ConfiguredPower<C, F> getPower(Identifier power);

	/**
	 * Returns a list of all powers currently on this entity.
	 * This includes both active and inactive powers.
	 *
	 * @return A {@link List} of the instances of {@link ConfiguredPower} currently on this component
	 */
	@NotNull
	@Contract(pure = true)
	List<ConfiguredPower<?, ?>> getPowers();

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
	Set<Identifier> getPowerTypes(boolean includeSubPowers);

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
	default <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> List<ConfiguredPower<C, F>> getPowers(F factory) {
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
	<C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> List<ConfiguredPower<C, F>> getPowers(F factory, boolean includeInactive);

	/**
	 * Returns a list of all sources for the given power.
	 *
	 * @param power The power to get the source for.
	 *
	 * @return A {@link List} of all sources of this power.
	 */
	@NotNull
	@Contract(pure = true)
	List<Identifier> getSources(Identifier power);

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
	default void readFromNbt(NbtCompound tag) {
		this.readNbt(tag, true);
	}

	/**
	 * Reads the data from this component to from the given tag.
	 *
	 * @param tag         The input tag.
	 * @param applyEvents Whether or not to apply {@link ConfiguredPower#onRemoved(LivingEntity)}, {@link ConfiguredPower#onLost(LivingEntity)},
	 *                    {@link ConfiguredPower#onAdded(LivingEntity)} and {@link ConfiguredPower#onGained(LivingEntity)}
	 */
	@Contract(mutates = "this")
	void readNbt(NbtCompound tag, boolean applyEvents);

	/**
	 * Writes this component to the given tag.
	 *
	 * @param tag The tag to write the data on.
	 */
	@Contract(mutates = "param")
	void writeToNbt(NbtCompound tag);

	@Contract(mutates = "param1")
	default void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		NbtCompound tag = new NbtCompound();
		this.writeToNbt(tag);
		buf.writeNbt(tag);
	}

	default void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
		if (tag != null)
			this.readNbt(tag, false);
	}

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
	@Nullable <T> T getPowerData(ConfiguredPower<?, ?> power, Supplier<? extends T> supplier);
}
