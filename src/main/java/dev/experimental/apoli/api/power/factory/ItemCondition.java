package dev.experimental.apoli.api.power.factory;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.architectury.core.RegistryEntry;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConditionData;
import dev.experimental.apoli.api.power.IConditionFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.item.ItemStack;

public abstract class ItemCondition<T extends IDynamicFeatureConfiguration> extends RegistryEntry<ItemCondition<?>> implements IConditionFactory<T, ConfiguredItemCondition<T, ?>, ItemCondition<T>> {
	public static final Codec<ItemCondition<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.ITEM_CONDITION);
	private final Codec<Pair<T, ConditionData>> codec;

	protected ItemCondition(Codec<T> codec) {
		this.codec = IConditionFactory.conditionCodec(codec);
	}

	@Override
	public Codec<Pair<T, ConditionData>> getConditionCodec() {
		return this.codec;
	}

	@Override
	public final ConfiguredItemCondition<T, ?> configure(T input, ConditionData data) {
		return new ConfiguredItemCondition<>(this, input, data);
	}

	public boolean check(T configuration, ItemStack stack) {
		return false;
	}

	public boolean check(T configuration, ConditionData data, ItemStack stack) {
		return data.inverted() ^ this.check(configuration, stack);
	}
}
