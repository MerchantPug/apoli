package dev.experimental.apoli.api.power;

import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface IVariableIntPower<T extends IDynamicFeatureConfiguration> {
	/**
	 * Changes the value, clamping it between bounds if applicable.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player to apply the operation to.
	 * @param value         The new value of this power.
	 *
	 * @return The new value of this power, after clamping.
	 */
	int assign(ConfiguredPower<T, ?> configuration, LivingEntity player, int value);

	/**
	 * Finds the current value of this power for the given player.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 *
	 * @return The current value of this power.
	 */
	int getValue(ConfiguredPower<T, ?> configuration, LivingEntity player);

	/**
	 * Find the maximum value for this power for the given player.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 *
	 * @return The maximum value of this power.
	 */
	int getMaximum(ConfiguredPower<T, ?> configuration, LivingEntity player);

	/**
	 * Find the minimum value for this power for the given player.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 *
	 * @return The minimum value of this power.
	 */
	int getMinimum(ConfiguredPower<T, ?> configuration, LivingEntity player);

	default float getProgress(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		int value = this.getValue(configuration, player);
		int min = this.getMinimum(configuration, player);
		int max = this.getMaximum(configuration, player);
		return Mth.clamp((value - min) / (float) (max - min), 0.0F, 1.0F);
	}

	/**
	 * Adds the given value to the current value.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 * @param amount        The amount to add to the current value. Can be negative.
	 *
	 * @return The new value of this power.
	 */
	default int change(ConfiguredPower<T, ?> configuration, LivingEntity player, int amount) {
		return this.assign(configuration, player, this.getValue(configuration, player) + amount);
	}

	/**
	 * Adds 1 to the current value.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 *
	 * @return The new value of this power.
	 */
	default int increment(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.change(configuration, player, 1);
	}


	/**
	 * Subtracts 1 from the current value.
	 *
	 * @param configuration The configuration of this power.
	 * @param player        The player get the value for.
	 *
	 * @return The new value of this power.
	 */
	default int decrement(ConfiguredPower<T, ?> configuration, LivingEntity player) {
		return this.change(configuration, player, -1);
	}
}
