package dev.experimental.apoli.common.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.component.IPowerContainer;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import io.github.apace100.apoli.Apoli;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PowerContainer implements IPowerContainer {
	private final LivingEntity owner;
	private final Map<Identifier, ConfiguredPower<?, ?>> powers;
	private final Map<Identifier, Set<Identifier>> powerSources;
	private final Map<Identifier, Object> powerData;

	public PowerContainer(LivingEntity owner) {
		this.owner = owner;
		this.powers = new ConcurrentHashMap<>();
		this.powerSources = new ConcurrentHashMap<>();
		this.powerData = new ConcurrentHashMap<>();
	}

	@Override
	public void removePower(Identifier power, Identifier source) {
		if (this.powerSources.containsKey(power)) {
			Set<Identifier> sources = this.powerSources.get(power);
			sources.remove(source);
			ConfiguredPower<?, ?> instance = this.powers.get(power);
			if (sources.isEmpty()) {
				this.powerSources.remove(power);
				if (instance != null) {
					instance.onRemoved(this.owner);
					instance.onLost(this.owner);
				}
			}
			Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers(this.owner.getServer());
			if (instance != null) {
				for (ConfiguredPower<?, ?> value : instance.getContainedPowers().values()) {
					Identifier id = powers.getId(value);
					if (id != null)
						this.removePower(id, source);
				}
			}
		}
	}

	@Override
	public int removeAllPowersFromSource(Identifier source) {
		List<Identifier> powersFromSource = this.getPowersFromSource(source);
		powersFromSource.forEach(power -> this.removePower(power, source));
		return powersFromSource.size();
	}

	@Override
	public @NotNull List<Identifier> getPowersFromSource(Identifier source) {
		return this.powerSources.entrySet().stream().filter(x -> x.getValue().contains(source)).map(Map.Entry::getKey).toList();
	}

	@Override
	public boolean addPower(Identifier power, Identifier source) {
		Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers(this.owner.getServer());
		Optional<ConfiguredPower<?, ?>> optionalInstance = powers.getOrEmpty(power);
		if (optionalInstance.isEmpty()) {
			Apoli.LOGGER.error("Trying to add unregistered power {} to entity {}", power, this.owner);
			return false;
		}
		ConfiguredPower<?, ?> instance = optionalInstance.get();
		if (this.powerSources.containsKey(power)) {
			Set<Identifier> sources = this.powerSources.get(power);
			if (sources.contains(source)) {
				return false;
			} else {
				sources.add(source);
				for (ConfiguredPower<?, ?> value : instance.getContainedPowers().values()) {
					Identifier id = powers.getId(value);
					if (id != null)
						this.addPower(id, source);
				}
				return true;
			}
		} else {
			for (ConfiguredPower<?, ?> value : instance.getContainedPowers().values()) {
				Identifier id = powers.getId(value);
				if (id != null)
					this.addPower(id, source);
			}
			Set<Identifier> sources = new HashSet<>();
			sources.add(source);
			this.powerSources.put(power, sources);
			this.powers.put(power, instance);
			instance.onGained(this.owner);
			instance.onAdded(this.owner);
			return true;
		}
	}

	@Override
	public boolean hasPower(Identifier power) {
		return this.powers.containsKey(power);
	}

	@Override
	public boolean hasPower(Identifier power, Identifier source) {
		return this.powerSources.containsKey(power) && this.powerSources.get(power).contains(source);
	}

	@Override
	@SuppressWarnings("unchecked")
	public @Nullable <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> ConfiguredPower<C, F> getPower(Identifier power) {
		if (this.powers.containsKey(power))
			return (ConfiguredPower<C, F>) this.powers.get(power);
		return null;
	}

	@Override
	public @NotNull List<ConfiguredPower<?, ?>> getPowers() {
		return ImmutableList.copyOf(this.powers.values());
	}

	@Override
	public @NotNull Set<Identifier> getPowerTypes(boolean includeSubPowers) {
		if (includeSubPowers)
			return ImmutableSet.copyOf(this.powers.keySet());
		Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers(this.owner.getServer());
		Set<Identifier> subPowers = this.powers.entrySet().stream().flatMap(x -> x.getValue().getChildren().stream().map(powers::getId).filter(Objects::nonNull)).collect(Collectors.toUnmodifiableSet());
		return this.powers.keySet().stream().filter(x -> !subPowers.contains(x)).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	@SuppressWarnings("unchecked")
	public @NotNull <C extends IDynamicFeatureConfiguration, F extends PowerFactory<C>> List<ConfiguredPower<C, F>> getPowers(F factory, boolean includeInactive) {
		ImmutableList.Builder<ConfiguredPower<C, F>> builder = ImmutableList.builder();
		this.powers.values().stream().filter(value -> Objects.equals(factory, value.getFactory()) && (includeInactive || value.isActive(this.owner))).map(value -> (ConfiguredPower<C, F>) value).forEach(builder::add);
		return builder.build();
	}

	@Override
	public @NotNull List<Identifier> getSources(Identifier power) {
		return this.powerSources.containsKey(power) ? ImmutableList.copyOf(this.powerSources.get(power)) : ImmutableList.of();
	}

	@Override
	public void sync() {
		ApoliAPI.synchronizePowerContainer(this.owner);
	}

	@Override
	public void serverTick() {
		for (ConfiguredPower<?, ?> value : this.powers.values()) {
			value.tick(this.owner);
		}
	}

	@Override
	public void readNbt(NbtCompound tag, boolean applyEvents) {
		try {
			if (this.owner == null) {
				Apoli.LOGGER.error("Owner was null in PowerHolderComponent#fromTag!");
			}
			if (applyEvents) {
				for (ConfiguredPower<?, ?> power : this.powers.values()) {
					power.onRemoved(this.owner);
					power.onLost(this.owner);
				}
			}
			this.powers.clear();
			NbtList powerList = (NbtList) tag.get("Powers");
			if (powerList != null) {
				Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers();
				for (int i = 0; i < powerList.size(); i++) {
					NbtCompound powerTag = powerList.getCompound(i);
					Identifier identifier = Identifier.tryParse(powerTag.getString("Type"));
					NbtList sources = (NbtList) powerTag.get("Sources");
					Set<Identifier> list = new HashSet<>();
					if (sources != null)
						sources.forEach(nbtElement -> list.add(Identifier.tryParse(nbtElement.asString())));
					this.powerSources.put(identifier, list);
					try {
						NbtElement data = powerTag.get("Data");
						Optional<ConfiguredPower<?, ?>> optionalPower = powers.getOrEmpty(identifier);
						if (optionalPower.isEmpty()) {
							Apoli.LOGGER.warn("Power data of unregistered power \"" + identifier + "\" found on entity, skipping...");
							continue;
						}
						ConfiguredPower<?, ?> instance = optionalPower.get();
						try {
							instance.deserialize(this.owner, data);
						} catch (ClassCastException e) {
							Apoli.LOGGER.warn("Data type of \"" + identifier + "\" changed, skipping data for that power on entity " + this.owner.getName().asString());
						}
						this.powers.put(identifier, instance);
						if (applyEvents)
							instance.onAdded(this.owner);
					} catch (IllegalArgumentException e) {
						Apoli.LOGGER.warn("Power data of unregistered power \"" + identifier + "\" found on entity, skipping...");
					}
				}
			}
		} catch (Exception e) {
			Apoli.LOGGER.info("Error while reading data: " + e.getMessage());
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList powerList = new NbtList();
		for (Map.Entry<Identifier, ConfiguredPower<?, ?>> powerEntry : this.powers.entrySet()) {
			NbtCompound powerTag = new NbtCompound();
			powerTag.putString("Type", powerEntry.getKey().toString());
			powerTag.put("Data", powerEntry.getValue().serialize(this.owner));
			NbtList sources = new NbtList();
			this.powerSources.get(powerEntry.getKey()).forEach(id -> sources.add(NbtString.of(id.toString())));
			powerTag.put("Sources", sources);
			powerList.add(powerTag);
		}
		tag.put("Powers", powerList);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> @Nullable T getPowerData(ConfiguredPower<?, ?> power, Supplier<? extends T> supplier) {
		return (T) this.powerData.computeIfAbsent(ApoliAPI.getPowers(this.owner.getServer()).getId(power), x -> supplier.get());
	}
}
