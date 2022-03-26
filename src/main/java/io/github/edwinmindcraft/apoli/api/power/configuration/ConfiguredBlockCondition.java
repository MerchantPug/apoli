package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.ApiStatus;
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

	@Contract("null, _ -> true")
	@Deprecated(forRemoval = true)
	@ApiStatus.ScheduledForRemoval(inVersion = "1.19")
	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, BlockInWorld block) {
		return condition == null || condition.check(block.getLevel(), block.getPos(), block::getState);
	}

	/**
	 * Checks if the block condition is valid at a given point.
	 *
	 * @param condition The condition to check for
	 * @param position  The {@link BlockPos position} to check the condition at
	 * @param state     The fast access to the {@link BlockState} to check the condition for
	 *
	 * @return The result of the check or {@code true} if the condition is {@code null}
	 *
	 * @see #check(LevelReader, BlockPos, NonNullSupplier) for a non-nullable version
	 * @see #check(ConfiguredBlockCondition, LevelReader, BlockPos) for a version that accesses the state from the world
	 */
	@Contract("null, _, _, _ -> true")
	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, LevelReader reader, BlockPos position, NonNullSupplier<BlockState> state) {
		return condition == null || condition.check(reader, position, state);
	}

	/**
	 * Checks if the block condition is valid at a given point.
	 *
	 * @param condition The condition to check for
	 * @param position  The {@link BlockPos position} to check the condition at
	 *
	 * @return The result of the check or {@code true} if the condition is {@code null}
	 *
	 * @see #check(ConfiguredBlockCondition, LevelReader, BlockPos, NonNullSupplier) for a version with a block state provider
	 */
	@Contract("null, _, _ -> true")
	public static boolean check(@Nullable ConfiguredBlockCondition<?, ?> condition, LevelReader reader, BlockPos position) {
		return check(condition, reader, position, () -> reader.getBlockState(position));
	}

	public ConfiguredBlockCondition(F factory, T configuration, ConditionData data) {
		super(factory, configuration, data);
	}

	@Deprecated(forRemoval = true)
	@ApiStatus.ScheduledForRemoval(inVersion = "1.19")
	public boolean check(BlockInWorld block) {
		return this.check(block.getLevel(), block.getPos(), block::getState);
	}

	/**
	 * Checks if the block condition is valid at a given point.
	 *
	 * @param reader   The {@link LevelReader level} to check the condition in
	 * @param position The {@link BlockPos position} to check the condition at
	 * @param state    The fast access to the {@link BlockState} to check the condition for
	 *
	 * @return The result of the check
	 *
	 * @see #check(ConfiguredBlockCondition, LevelReader, BlockPos, NonNullSupplier)  for a nullable version
	 */
	public boolean check(LevelReader reader, BlockPos position, NonNullSupplier<BlockState> state) {
		return this.getFactory().check(this, reader, position, state);
	}


	@Override
	public String toString() {
		return "CBC:" + this.getFactory().getRegistryName() + "(" + this.getData() + ")-" + this.getConfiguration();
	}
}