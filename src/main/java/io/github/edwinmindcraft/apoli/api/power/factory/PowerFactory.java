package io.github.edwinmindcraft.apoli.api.power.factory;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IFactory;
import io.github.edwinmindcraft.apoli.api.power.PowerData;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Map;

public abstract class PowerFactory<T extends IDynamicFeatureConfiguration> extends ForgeRegistryEntry<PowerFactory<?>> {
	public static final Codec<PowerFactory<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.POWER_FACTORY);

	private static <T extends IDynamicFeatureConfiguration, F extends PowerFactory<T>> Codec<ConfiguredPower<T, ?>> powerCodec(Codec<T> codec, F factory) {
		return IFactory.unionCodec(IFactory.asMap(codec), PowerData.CODEC, factory::configure, ConfiguredPower::getConfiguration, ConfiguredPower::getData);
	}

	private final Codec<ConfiguredPower<T, ?>> codec;
	private final boolean allowConditions;
	private boolean ticking = false;
	private boolean tickingWhenInactive = false;

	protected PowerFactory(Codec<T> codec) {
		this(codec, true);
	}

	/**
	 * Creates a new power factory.
	 *
	 * @param codec           The codec used to serialize the configuration of this power.
	 * @param allowConditions Determines whether this power will use the global field {@link PowerData#conditions()} or not.
	 *
	 * @see #PowerFactory(Codec) for a version with allow conditions true by default.
	 */
	protected PowerFactory(Codec<T> codec, boolean allowConditions) {
		this.codec = powerCodec(codec, this);
		this.allowConditions = allowConditions;
	}

	/**
	 * Marks this power has having a ticking function, if this isn't done,
	 * the mod won't bother calling the {@link #tick(ConfiguredPower, LivingEntity)} function.
	 *
	 * @param whenInactive If true, tick will bypass the check to {@link #isActive(ConfiguredPower, LivingEntity)}
	 *
	 * @see #ticking() for a version that sets whenInactive to false.
	 */
	protected final void ticking(boolean whenInactive) {
		this.ticking = true;
		this.tickingWhenInactive = whenInactive;
	}

	public Codec<ConfiguredPower<T, ?>> getCodec() {
		return this.codec;
	}

	/**
	 * Marks this power has having a ticking function, if this isn't done,
	 * the mod won't bother calling the {@link #tick(ConfiguredPower, LivingEntity)} function.
	 *
	 * @see #ticking(boolean) for a version that allows ticking when this power is inactive.
	 */
	protected final void ticking() {
		this.ticking(false);
	}

	/**
	 * Returns a map containing children of this power.<br/>
	 * Origins uses this for the "multiple" power type, which contains children.
	 *
	 * @param configuration The configuration of this power.
	 *
	 * @return A map containing children of this power.
	 */
	public Map<String, ConfiguredPower<?, ?>> getContainedPowers(ConfiguredPower<T, ?> configuration) {
		return ImmutableMap.of();
	}

	public ConfiguredPower<T, ?> configure(T input, PowerData data) {
		return new ConfiguredPower<>(this, input, data);
	}

	protected boolean shouldCheckConditions(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.allowConditions;
	}

	protected boolean shouldTickWhenActive(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.ticking;
	}

	protected boolean shouldTickWhenInactive(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.tickingWhenInactive;
	}

	public boolean canTick(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.shouldTickWhenActive(configuration, entity) && (this.shouldTickWhenInactive(configuration, entity) || this.isActive(configuration, entity));
	}

	public void tick(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.tick(configuration.getConfiguration(), entity);
	}

	public void onGained(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.onGained(configuration.getConfiguration(), entity);
	}

	public void onLost(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.onLost(configuration.getConfiguration(), entity);
	}

	public void onAdded(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.onAdded(configuration.getConfiguration(), entity);
	}

	public void onRemoved(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.onRemoved(configuration.getConfiguration(), entity);
	}

	public void onRespawn(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		this.onRespawn(configuration.getConfiguration(), entity);
	}

	public int tickInterval(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return this.tickInterval(configuration.getConfiguration(), entity);
	}

	protected void tick(T configuration, LivingEntity entity) {}

	protected void onGained(T configuration, LivingEntity entity) {}

	protected void onLost(T configuration, LivingEntity entity) {}

	protected void onAdded(T configuration, LivingEntity entity) {}

	protected void onRemoved(T configuration, LivingEntity entity) {}

	protected void onRespawn(T configuration, LivingEntity entity) {}

	protected int tickInterval(T configuration, LivingEntity entity) {return 1;}

	public boolean isActive(ConfiguredPower<T, ?> configuration, LivingEntity entity) {
		return !this.shouldCheckConditions(configuration, entity) || configuration.getData().conditions().stream().allMatch(condition -> condition.check(entity));
	}

	public Tag serialize(ConfiguredPower<T, ?> configuration, LivingEntity entity, IPowerContainer container) {
		return new CompoundTag();
	}

	public void deserialize(ConfiguredPower<T, ?> configuration, LivingEntity entity, IPowerContainer container, Tag tag) {}

	private final Lazy<io.github.apace100.apoli.power.factory.PowerFactory<?>> legacyType = Lazy.of(() -> new io.github.apace100.apoli.power.factory.PowerFactory<>(this.getRegistryName(), this));

	public io.github.apace100.apoli.power.factory.PowerFactory<?> getLegacyFactory() {
		return this.legacyType.get();
	}
}
