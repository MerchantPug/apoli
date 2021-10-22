package io.github.apace100.apoli.power.factory;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.fabric.FabricPowerConfiguration;
import io.github.edwinmindcraft.apoli.fabric.FabricPowerFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.Lazy;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PowerFactory<P extends Power> {

	private final ResourceLocation id;
	private final Lazy<FabricPowerFactory<P>> wrapped;
	private boolean hasConditions = false;
	protected SerializableData data;
	protected Function<SerializableData.Instance, BiFunction<PowerType<P>, LivingEntity, P>> factoryConstructor;

	public PowerFactory(ResourceLocation id, SerializableData data, Function<SerializableData.Instance, BiFunction<PowerType<P>, LivingEntity, P>> factoryConstructor) {
		this.id = id;
		this.data = data;
		this.factoryConstructor = factoryConstructor;
		this.wrapped = Lazy.of(() -> new FabricPowerFactory<>(FabricPowerConfiguration.codec(data, factoryConstructor), this.hasConditions));
	}

	public PowerFactory<P> allowCondition() {
		if (!this.hasConditions) this.hasConditions = true;
		return this;
	}

	public FabricPowerFactory<P> getWrapped() {
		return this.wrapped.get();
	}

	public ResourceLocation getSerializerId() {
		return this.id;
	}

	public class Instance implements BiFunction<PowerType<P>, LivingEntity, P> {

		public final ConfiguredPower<?, ?> power;

		private Instance(ConfiguredPower<?, ?> power) {
			this.power = power;
		}

		public ConfiguredPower<?, ?> getPower() {
			return this.power;
		}

		@SuppressWarnings("unchecked")
		@Override
		public P apply(PowerType<P> pPowerType, LivingEntity livingEntity) {
			if (this.power.getConfiguration() instanceof FabricPowerConfiguration config)
				return (P) this.power.getPowerData(livingEntity, () -> config.power().apply(pPowerType, livingEntity));
			return null;
		}
	}
}
