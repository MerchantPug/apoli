package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.OptionalFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ModifyCraftingConfiguration(@Nullable ResourceLocation recipeIdentifier,
										  @Nullable ConfiguredItemCondition<?, ?> itemCondition,
										  @Nullable ItemStack newStack,
										  @Nullable ConfiguredItemAction<?, ?> itemAction,
										  @Nullable ConfiguredEntityAction<?, ?> entityAction,
										  @Nullable ConfiguredBlockAction<?, ?> blockAction) implements IDynamicFeatureConfiguration {
	public static final Codec<ModifyCraftingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(SerializableDataTypes.IDENTIFIER, "recipe").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::recipeIdentifier)),
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::itemCondition)),
			CalioCodecHelper.optionalField(SerializableDataTypes.ITEM_STACK, "result").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::newStack)),
			CalioCodecHelper.optionalField(ConfiguredItemAction.CODEC, "item_action").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::itemAction)),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::entityAction)),
			CalioCodecHelper.optionalField(ConfiguredBlockAction.CODEC, "block_action").forGetter(OptionalFuncs.opt(ModifyCraftingConfiguration::blockAction))
	).apply(instance, OptionalFuncs.of(ModifyCraftingConfiguration::new)));

	public boolean doesApply(CraftingContainer container, Recipe<? super CraftingContainer> recipe, Level level) {
		return (this.recipeIdentifier() == null || Objects.equals(recipe.getId(), this.recipeIdentifier())) &&
			   (this.itemCondition() == null || !this.itemCondition().check(level, recipe.assemble(container)));
	}

	public ItemStack createResult(CraftingContainer container, Recipe<? super CraftingContainer> recipe, Level level) {
		Mutable<ItemStack> stack = new MutableObject<>();
		if (this.newStack() != null)
			stack.setValue(this.newStack().copy());
		else
			stack.setValue(recipe.assemble(container));
		ConfiguredItemAction.execute(this.itemAction(), level, stack);
		return stack.getValue();
	}

	public void execute(Entity entity, @Nullable BlockPos pos) {
		if (pos != null && this.blockAction() != null)
			this.blockAction().execute(entity.level, pos, Direction.UP);
		ConfiguredEntityAction.execute(this.entityAction(), entity);
	}
}
