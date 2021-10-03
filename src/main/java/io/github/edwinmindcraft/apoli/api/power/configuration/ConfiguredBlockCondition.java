package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public final class ConfiguredBlockCondition<T extends IDynamicFeatureConfiguration, F extends BlockCondition<T>> extends ConfiguredCondition<T, F> {
	public static final Codec<ConfiguredBlockCondition<?, ?>> CODEC = BlockCondition.CODEC.dispatch(ConfiguredBlockCondition::getFactory, BlockCondition::getConditionCodec);

	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, BlockInWorld position) {
		return condition == null || condition.check(position);
	}

	public ConfiguredBlockCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	public boolean check(BlockInWorld block) {
		return this.getFactory().check(this, block);
	}

	@Override
	public String toString() {
		return "CBC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}