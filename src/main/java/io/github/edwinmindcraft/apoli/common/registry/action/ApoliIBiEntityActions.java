package io.github.edwinmindcraft.apoli.common.registry.action;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.action.bientity.DamageAction;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.action.bientity.*;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.BIENTITY_ACTIONS;

public class ApoliIBiEntityActions {
	public static final BiConsumer<ConfiguredBiEntityAction<?, ?>, Pair<Entity, Entity>> EXECUTOR = (action, pair) -> action.execute(pair.getFirst(), pair.getSecond());
	public static final BiPredicate<ConfiguredBiEntityCondition<?, ?>, Pair<Entity, Entity>> PREDICATE = (condition, pair) -> condition.check(pair.getFirst(), pair.getSecond());

	private static <U extends BiEntityAction<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.BIENTITY_ACTION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedBiEntityAction<StreamConfiguration<ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> AND = of("and");
	public static final RegistryObject<DelegatedBiEntityAction<ChanceConfiguration<ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> CHANCE = of("chance");
	public static final RegistryObject<DelegatedBiEntityAction<IfElseConfiguration<ConfiguredItemCondition<?, ?>, ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> IF_ELSE = of("if_else");
	public static final RegistryObject<DelegatedBiEntityAction<StreamConfiguration<ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> IF_ELSE_LIST = of("if_else_list");
	public static final RegistryObject<DelegatedBiEntityAction<ChoiceConfiguration<ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> CHOICE = of("choice");
	public static final RegistryObject<DelegatedBiEntityAction<DelayAction<ConfiguredItemAction<?, ?>, Pair<Entity, Entity>>>> DELAY = of("delay");
	public static final RegistryObject<DelegatedBiEntityAction<NothingConfiguration<Pair<Entity, Entity>>>> NOTHING = of("nothing");

	public static final RegistryObject<InvertBiEntityAction> INVERT = BIENTITY_ACTIONS.register("invert", InvertBiEntityAction::new);
	public static final RegistryObject<DispatchBiEntityAction> ACTOR_ACTION = BIENTITY_ACTIONS.register("actor_action", DispatchBiEntityAction::actor);
	public static final RegistryObject<DispatchBiEntityAction> TARGET_ACTION = BIENTITY_ACTIONS.register("target_action", DispatchBiEntityAction::target);
	public static final RegistryObject<SimpleBiEntityAction> MOUNT = BIENTITY_ACTIONS.register("mount", () -> new SimpleBiEntityAction(SimpleBiEntityAction::mount));
	public static final RegistryObject<SimpleBiEntityAction> SET_IN_LOVE = BIENTITY_ACTIONS.register("set_in_love", () -> new SimpleBiEntityAction(SimpleBiEntityAction::setInLove));
	public static final RegistryObject<SimpleBiEntityAction> TAME = BIENTITY_ACTIONS.register("tame", () -> new SimpleBiEntityAction(SimpleBiEntityAction::tame));
	public static final RegistryObject<AddVelocityAction> ADD_VELOCITY = BIENTITY_ACTIONS.register("add_velocity", AddVelocityAction::new);
	public static final RegistryObject<DamageAction> DAMAGE = BIENTITY_ACTIONS.register("damage", DamageAction::new);


	public static void bootstrap() {
		MetaFactories.defineMetaActions(BIENTITY_ACTIONS, DelegatedBiEntityAction::new, ConfiguredBiEntityAction.CODEC, ConfiguredBiEntityCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
