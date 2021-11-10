package io.github.edwinmindcraft.apoli.fabric;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WrappingRegistry<T, F extends IForgeRegistryEntry<F>> extends WritableRegistry<T> {
	private final Lifecycle lifecycle;
	private final Lazy<IForgeRegistry<F>> registry;
	private final Class<F> forgeClass;
	private final Function<T, F> from;
	private final Function<F, T> to;
	private final Map<String, DeferredRegister<F>> registerMap;

	public WrappingRegistry(ResourceKey<? extends Registry<T>> registryKey, Lifecycle lifecycle, Supplier<IForgeRegistry<F>> registry, Class<F> forgeClass, Function<T, F> from, Function<F, T> to) {
		super(registryKey, lifecycle);
		this.lifecycle = lifecycle;
		this.registry = Lazy.of(registry);
		this.forgeClass = forgeClass;
		this.from = from;
		this.to = to;
		this.registerMap = new HashMap<>();
	}

	private DeferredRegister<F> createRegister(String modid) {
		DeferredRegister<F> register = DeferredRegister.create(this.forgeClass, modid);
		register.register(FMLJavaModLoadingContext.get().getModEventBus());
		return register;
	}

	@Override
	public <V extends T> V registerMapping(int id, ResourceKey<T> resourceKey, V value, Lifecycle lifecycle) {
		return this.register(resourceKey, value, lifecycle);
	}

	@Override
	public <V extends T> V register(ResourceKey<T> resourceKey, V value, Lifecycle lifecycle) {
		this.registerMap.computeIfAbsent(resourceKey.location().getNamespace(), this::createRegister).register(resourceKey.location().getPath(), () -> this.from.apply(value));
		return value;
	}

	@Override
	public <V extends T> V registerOrOverride(OptionalInt id, ResourceKey<T> resourceKey, V value, Lifecycle lifecycle) {
		return this.register(resourceKey, value, lifecycle);
	}

	@Override
	public boolean isEmpty() {
		return this.registry.get().isEmpty();
	}

	@Nullable
	@Override
	public ResourceLocation getKey(T object) {
		return this.registry.get().getKey(this.from.apply(object));
	}

	@Override
	public Optional<ResourceKey<T>> getResourceKey(T object) {
		ResourceLocation key = this.getKey(object);
		return key == null ? Optional.empty() : Optional.of(ResourceKey.create(this.key(), key));
	}

	@Override
	public int getId(@Nullable T object) {
		return this.registry.get() instanceof ForgeRegistry<F> fr ? fr.getID(this.from.apply(object)) : -1;
	}

	@Nullable
	@Override
	public T byId(int id) {
		return this.registry.get() instanceof ForgeRegistry<F> fr ? this.to.apply(fr.getValue(id)) : null;
	}

	@Nullable
	@Override
	public T get(@Nullable ResourceKey<T> resourceKey) {
		return this.get(resourceKey == null ? null : resourceKey.location());
	}

	@Nullable
	@Override
	public T get(@Nullable ResourceLocation location) {
		return this.to.apply(this.registry.get().getValue(location));
	}

	@Override
	protected Lifecycle lifecycle(T p_123012_) {
		return this.lifecycle;
	}

	@Override
	public Lifecycle elementsLifecycle() {
		return this.lifecycle;
	}

	@Override
	public Set<ResourceLocation> keySet() {
		return this.registry.get().getKeys();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
		return this.registry.get().getEntries().stream().map(x -> Map.entry((ResourceKey<T>) x.getKey(), this.to.apply(x.getValue()))).collect(Collectors.toSet());
	}

	@Nullable
	@Override
	public T getRandom(Random random) {
		List<F> values = this.registry.get().getValues().stream().toList();
		return values.isEmpty() ? null : this.to.apply(values.get(random.nextInt(values.size())));
	}

	@Override
	public boolean containsKey(ResourceLocation location) {
		return this.registry.get().containsKey(location);
	}

	@Override
	public boolean containsKey(ResourceKey<T> resourceKey) {
		return this.registry.get().containsKey(resourceKey.location());
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return this.registry.get().getValues().stream().map(this.to).iterator();
	}
}
