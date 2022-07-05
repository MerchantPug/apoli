package io.github.apace100.apoli.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.util.ModifiedCraftingRecipe;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

	@Shadow
	protected abstract <C extends Container, T extends Recipe<C>> Map<ResourceLocation, T> byType(RecipeType<T> p_44055_);

	@Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	private <C extends Container, T extends Recipe<C>> void prioritizeModifiedRecipes(RecipeType<T> type, C inventory, Level world, CallbackInfoReturnable<Optional<T>> cir) {
		Optional<T> modifiedRecipe = this.byType(type).values().stream()
				.filter((recipe) -> recipe.matches(inventory, world))
				.filter(r -> r.getClass() == ModifiedCraftingRecipe.class).findFirst();
		if (modifiedRecipe.isPresent())
			cir.setReturnValue(modifiedRecipe);
	}

	@Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	private <C extends Container, T extends Recipe<C>> void prioritizeModifiedRecipes(RecipeType<T> type, C inventory, Level world, ResourceLocation location, CallbackInfoReturnable<Optional<Pair<ResourceLocation, T>>> cir) {
		if (location == null) {
			Optional<Pair<ResourceLocation, T>> modifiedRecipe = this.byType(type).entrySet().stream()
					.filter((recipe) -> recipe.getValue().matches(inventory, world))
					.filter(r -> r.getValue().getClass() == ModifiedCraftingRecipe.class).findFirst()
					.map(kvp -> Pair.of(kvp.getKey(), kvp.getValue()));
			if (modifiedRecipe.isPresent())
				cir.setReturnValue(modifiedRecipe);
		}
	}
}
