package io.github.apace100.apoli.access;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFoodConfiguration;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ModifiableFoodEntity {

	ItemStack getOriginalFoodStack();

	void setOriginalFoodStack(ItemStack original);

	List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> getCurrentModifyFoodPowers();

	void setCurrentModifyFoodPowers(List<ConfiguredPower<ModifyFoodConfiguration, ModifyFoodPower>> powers);

	void enforceFoodSync();

	void resetFoodSync();

	boolean shouldSyncFood();
}
