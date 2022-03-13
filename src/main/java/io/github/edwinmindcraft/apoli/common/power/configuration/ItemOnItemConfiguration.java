package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ItemOnItemConfiguration(@Nullable ConfiguredItemCondition<?, ?> usingItemCondition,
									  @Nullable ConfiguredItemCondition<?, ?> onItemCondition,
									  int resultFromOnStack,
									  @Nullable ItemStack newStack,
									  @Nullable ConfiguredItemAction<?, ?> usingItemAction,
									  @Nullable ConfiguredItemAction<?, ?> onItemAction,
									  @Nullable ConfiguredItemAction<?, ?> resultItemAction,
									  @Nullable ConfiguredEntityAction<?, ?> entityAction) implements IDynamicFeatureConfiguration {
	public static final Codec<ItemOnItemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "using_item_condition").forGetter(x -> Optional.ofNullable(x.usingItemCondition())),
			CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "on_item_condition").forGetter(x -> Optional.ofNullable(x.onItemCondition())),
			CalioCodecHelper.optionalField(Codec.INT, "result_from_on_stack", 0).forGetter(ItemOnItemConfiguration::resultFromOnStack),
			CalioCodecHelper.optionalField(SerializableDataTypes.ITEM_STACK, "result").forGetter(x -> Optional.ofNullable(x.newStack())),
			CalioCodecHelper.optionalField(ConfiguredItemAction.CODEC, "using_item_action").forGetter(x -> Optional.ofNullable(x.usingItemAction())),
			CalioCodecHelper.optionalField(ConfiguredItemAction.CODEC, "on_item_action").forGetter(x -> Optional.ofNullable(x.onItemAction())),
			CalioCodecHelper.optionalField(ConfiguredItemAction.CODEC, "result_item_action").forGetter(x -> Optional.ofNullable(x.resultItemAction())),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction()))
	).apply(instance, (t1, t2, t3, t4, t5, t6, t7, t8) -> new ItemOnItemConfiguration(
			t1.orElse(null),
			t2.orElse(null),
			t3,
			t4.orElse(null),
			t5.orElse(null),
			t6.orElse(null),
			t7.orElse(null),
			t8.orElse(null)
	)));

	public boolean check(Level level, ItemStack using, ItemStack on) {
		return ConfiguredItemCondition.check(this.usingItemCondition(), level, using) && ConfiguredItemCondition.check(this.onItemCondition(), level, on);
	}

	public void execute(Entity entity, Mutable<ItemStack> using, Mutable<ItemStack> on, Slot slot) {
		Mutable<ItemStack> stack = new MutableObject<>(ItemStack.EMPTY);
		if (this.newStack() != null)
			stack.setValue(this.newStack().copy());
		else if (this.resultFromOnStack() > 0)
			stack.setValue(on.getValue().split(this.resultFromOnStack()));
		else
			stack.setValue(on.getValue());
		ConfiguredItemAction.execute(this.resultItemAction(), entity.level, stack);
		ConfiguredItemAction.execute(this.usingItemAction(), entity.level, using);
		ConfiguredItemAction.execute(this.onItemAction(), entity.level, on);
		if (this.newStack() != null || this.resultItemAction() != null) {
			if (slot.getItem().isEmpty())
				slot.set(stack.getValue());
			else if (entity instanceof Player player)
				player.getInventory().placeItemBackInInventory(stack.getValue());
		}
		ConfiguredEntityAction.execute(this.entityAction(), entity);
	}
}
