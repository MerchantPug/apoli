package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.util.ModifiedCraftingRecipe;
import net.minecraft.Util;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

	@Shadow
	public abstract <C extends Container, T extends Recipe<C>> List<T> getAllRecipesFor(RecipeType<T> pRecipeType);

	@Inject(method = "getRecipeFor", at = @At("HEAD"), cancellable = true)
	private void prioritizeModifiedRecipes(RecipeType<Recipe<Container>> type, Container inventory, Level world, CallbackInfoReturnable<Optional<Recipe<Container>>> cir) {
		Optional<Recipe<Container>> modifiedRecipe = this.getAllRecipesFor(type).stream()
				.flatMap((recipe) -> type.tryMatch(recipe, world, inventory).stream())
				.filter(r -> r.getClass() == ModifiedCraftingRecipe.class).findFirst();
		if (modifiedRecipe.isPresent())
			cir.setReturnValue(modifiedRecipe);
	}
}
