package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class ConfiguredItemCondition<C extends IDynamicFeatureConfiguration, F extends ItemCondition<C>> extends ConfiguredCondition<C, F> {
	public static final Codec<ConfiguredItemCondition<?, ?>> CODEC = ItemCondition.CODEC.dispatch(ConfiguredItemCondition::getFactory, ItemCondition::getConditionCodec);

	public static boolean check(@Nullable ConfiguredItemCondition<?, ?> condition, ItemStack stack) {
		return condition == null || condition.check(stack);
	}

	public ConfiguredItemCondition(F factory, C configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(ItemStack stack) {
		return this.getFactory().check(this.getConfiguration(), this.getData(), stack);
	}

	@Override
	public String toString() {
		return "CIC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}