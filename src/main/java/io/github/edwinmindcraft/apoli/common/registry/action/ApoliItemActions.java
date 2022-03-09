package io.github.edwinmindcraft.apoli.common.registry.action;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.action.meta.NothingAction;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.action.item.ConsumeItemAction;
import io.github.edwinmindcraft.apoli.common.action.item.DamageItemAction;
import io.github.edwinmindcraft.apoli.common.action.item.DelegatedItemAction;
import io.github.edwinmindcraft.apoli.common.action.item.ModifyItemAction;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.Mutable;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ITEM_ACTIONS;

public class ApoliItemActions {
	public static final BiConsumer<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>> EXECUTOR = (action, pair) -> action.execute(pair.getFirst(), pair.getSecond());
	public static final BiPredicate<ConfiguredItemCondition<?, ?>, Pair<Level, Mutable<ItemStack>>> PREDICATE = (condition, pair) -> condition.check(pair.getFirst(), pair.getSecond().getValue());

	private static <U extends ItemAction<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.ITEM_ACTION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedItemAction<StreamConfiguration<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> AND = of("and");
	public static final RegistryObject<DelegatedItemAction<ChanceConfiguration<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> CHANCE = of("chance");
	public static final RegistryObject<DelegatedItemAction<IfElseConfiguration<ConfiguredItemCondition<?, ?>, ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> IF_ELSE = of("if_else");
	public static final RegistryObject<DelegatedItemAction<StreamConfiguration<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> IF_ELSE_LIST = of("if_else_list");
	public static final RegistryObject<DelegatedItemAction<ChoiceConfiguration<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> CHOICE = of("choice");
	public static final RegistryObject<DelegatedItemAction<DelayAction<ConfiguredItemAction<?, ?>, Pair<Level, Mutable<ItemStack>>>>> DELAY = of("delay");
	public static final RegistryObject<DelegatedItemAction<NothingConfiguration<Pair<Level, Mutable<ItemStack>>>>> NOTHING = of("nothing");

	public static final RegistryObject<ConsumeItemAction> CONSUME = ITEM_ACTIONS.register("consume", ConsumeItemAction::new);
	public static final RegistryObject<ModifyItemAction> MODIFY = ITEM_ACTIONS.register("modify", ModifyItemAction::new);
	public static final RegistryObject<DamageItemAction> DAMAGE = ITEM_ACTIONS.register("damage", DamageItemAction::new);

	public static void register() {
		MetaFactories.defineMetaActions(ITEM_ACTIONS, DelegatedItemAction::new, ConfiguredItemAction.CODEC, ConfiguredItemCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
