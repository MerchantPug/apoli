package io.github.edwinmindcraft.apoli.common.registry;

import io.github.apace100.apoli.util.ModifiedCraftingRecipe;
import io.github.apace100.apoli.util.PowerRestrictedCraftingRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.RECIPE_SERIALIZERS;

public class ApoliRecipeSerializers {
	public static final RegistryObject<SimpleRecipeSerializer<PowerRestrictedCraftingRecipe>> POWER_RESTRICTED = RECIPE_SERIALIZERS.register("power_restricted", () -> new SimpleRecipeSerializer<>(PowerRestrictedCraftingRecipe::new));
	public static final RegistryObject<SimpleRecipeSerializer<ModifiedCraftingRecipe>> MODIFIED = RECIPE_SERIALIZERS.register("modified", () -> new SimpleRecipeSerializer<>(ModifiedCraftingRecipe::new));

	public static void bootstrap() {}
}
