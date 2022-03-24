package io.github.edwinmindcraft.apoli.common.condition.item;

import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SimpleItemCondition extends ItemCondition<NoConfiguration> {
	public static boolean forCookingRecipeType(Level level, ItemStack stack, RecipeType<? extends AbstractCookingRecipe> type) {
		if (level == null) return false;
		return level.getRecipeManager().getRecipeFor(type, new SimpleContainer(stack), level).isPresent();
	}

	private final BiPredicate<Level, ItemStack> predicate;

	public SimpleItemCondition(Predicate<ItemStack> predicate) {
		this((level, stack) -> predicate.test(stack));
	}

	public SimpleItemCondition(BiPredicate<Level, ItemStack> predicate) {
		super(NoConfiguration.CODEC);
		this.predicate = predicate;
	}

	@Override
	public boolean check(NoConfiguration configuration, @Nullable Level level, ItemStack stack) {
		return this.predicate.test(level, stack);
	}
}
