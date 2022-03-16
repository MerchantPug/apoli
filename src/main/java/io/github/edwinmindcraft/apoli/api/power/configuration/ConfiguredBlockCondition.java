package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a block condition.
 *
 * @param <T> The configuration of this condition
 * @param <F> The executor of this condition.
 *
 * @author Edwin
 */
public final class ConfiguredBlockCondition<T extends IDynamicFeatureConfiguration, F extends BlockCondition<T>> extends ConfiguredCondition<T, F> {
	public static final Codec<ConfiguredBlockCondition<?, ?>> CODEC = BlockCondition.CODEC.dispatch(ConfiguredBlockCondition::getFactory, BlockCondition::getConditionCodec);
	public static final MapCodec<Optional<ConfiguredBlockCondition<?, ?>>> OPTIONAL_FIELD = CalioCodecHelper.optionalField(CODEC, "block_condition");

	/**
	 * Checks if the block condition is valid at a given point.
	 *
	 * @param condition The condition to check for
	 * @param block     The {@link BlockInWorld block} to check the condition at
	 *
	 * @return The result of the check or {@code true} if the condition is {@code null}
	 *
	 * @see #check(BlockInWorld) for a non-nullable version
	 */
	@Contract("null, _ -> true")
	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, BlockInWorld block) {
		return condition == null || condition.check(block);
	}

	public ConfiguredBlockCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	/**
	 * Checks if the block condition is valid at a given point.
	 *
	 * @param block The {@link BlockInWorld block} to check the condition at
	 *
	 * @return The result of the check
	 * @see #check(ConfiguredBlockCondition, BlockInWorld) for a nullable version
	 */
	public boolean check(BlockInWorld block) {
		return this.getFactory().check(this, block);
	}

	@Override
	public String toString() {
		return "CBC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}