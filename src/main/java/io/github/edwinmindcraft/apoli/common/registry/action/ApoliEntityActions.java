package io.github.edwinmindcraft.apoli.common.registry.action;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.entity.AreaOfEffectAction;
import io.github.apace100.apoli.action.entity.CraftingTableAction;
import io.github.apace100.apoli.action.entity.EnderChestAction;
import io.github.apace100.apoli.power.factory.action.entity.RaycastAction;
import io.github.apace100.apoli.power.factory.action.entity.SpawnParticlesAction;
import io.github.apace100.apoli.power.factory.action.entity.SwingHandAction;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.action.configuration.ChangeResourceConfiguration;
import io.github.edwinmindcraft.apoli.common.action.entity.*;
import io.github.edwinmindcraft.apoli.common.action.meta.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters.ENTITY_ACTIONS;

public class ApoliEntityActions {
	public static final BiConsumer<ConfiguredEntityAction<?, ?>, Entity> EXECUTOR = (action, entity) -> action.execute(entity);
	public static final BiPredicate<ConfiguredEntityCondition<?, ?>, Entity> PREDICATE = (condition, entity) -> condition.check(entity);

	private static <U extends EntityAction<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.ENTITY_ACTION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedEntityAction<StreamConfiguration<ConfiguredEntityAction<?, ?>, Entity>>> AND = of("and");
	public static final RegistryObject<DelegatedEntityAction<ChanceConfiguration<ConfiguredEntityAction<?, ?>, Entity>>> CHANCE = of("chance");
	public static final RegistryObject<DelegatedEntityAction<IfElseConfiguration<ConfiguredEntityCondition<?, ?>, ConfiguredEntityAction<?, ?>, Entity>>> IF_ELSE = of("if_else");
	public static final RegistryObject<DelegatedEntityAction<StreamConfiguration<ConfiguredEntityAction<?, ?>, Entity>>> IF_ELSE_LIST = of("if_else_list");
	public static final RegistryObject<DelegatedEntityAction<ChoiceConfiguration<ConfiguredEntityAction<?, ?>, Entity>>> CHOICE = of("choice");
	public static final RegistryObject<DelegatedEntityAction<DelayAction<ConfiguredEntityAction<?, ?>, Entity>>> DELAY = of("delay");
	public static final RegistryObject<DelegatedEntityAction<NothingConfiguration<Entity>>> NOTHING = of("nothing");

	public static final RegistryObject<AddVelocityAction> ADD_VELOCITY = ENTITY_ACTIONS.register("add_velocity", AddVelocityAction::new);
	public static final RegistryObject<AddExperienceAction> ADD_EXPERIENCE = ENTITY_ACTIONS.register("add_xp", AddExperienceAction::new);
	public static final RegistryObject<ApplyEffectAction> APPLY_EFFECT = ENTITY_ACTIONS.register("apply_effect", ApplyEffectAction::new);
	public static final RegistryObject<BlockActionAtAction> BLOCK_ACTION_AT = ENTITY_ACTIONS.register("block_action_at", BlockActionAtAction::new);
	public static final RegistryObject<ChangeResourceAction> CHANGE_RESOURCE = ENTITY_ACTIONS.register("change_resource", () -> new ChangeResourceAction(ChangeResourceConfiguration.ANY_CODEC));
	public static final RegistryObject<ClearEffectAction> CLEAR_EFFECT = ENTITY_ACTIONS.register("clear_effect", ClearEffectAction::new);
	public static final RegistryObject<SimpleEntityAction> EXTINGUISH = ENTITY_ACTIONS.register("extinguish", () -> new SimpleEntityAction(Entity::clearFire));
	public static final RegistryObject<ExecuteCommandEntityAction> EXECUTE_COMMAND = ENTITY_ACTIONS.register("execute_command", ExecuteCommandEntityAction::new);
	public static final RegistryObject<IntegerEntityAction> SET_ON_FIRE = ENTITY_ACTIONS.register("set_on_fire", () -> new IntegerEntityAction(Entity::setSecondsOnFire, "duration"));
	public static final RegistryObject<FloatEntityAction> EXHAUST = ENTITY_ACTIONS.register("exhaust", () -> FloatEntityAction.ofPlayer((x, f) -> x.getFoodData().addExhaustion(f), "amount"));
	public static final RegistryObject<FloatEntityAction> HEAL = ENTITY_ACTIONS.register("heal", () -> FloatEntityAction.ofLiving(LivingEntity::heal, "amount"));
	public static final RegistryObject<IntegerEntityAction> GAIN_AIR = ENTITY_ACTIONS.register("gain_air", () -> IntegerEntityAction.ofLiving((x, f) -> x.setAirSupply(Math.min(x.getAirSupply() + f, x.getMaxAirSupply())), "value"));
	public static final RegistryObject<FloatEntityAction> SET_FALL_DISTANCE = ENTITY_ACTIONS.register("set_fall_distance", () -> new FloatEntityAction((entity, f) -> entity.fallDistance = f, "fall_distance"));
	public static final RegistryObject<DamageAction> DAMAGE = ENTITY_ACTIONS.register("damage", DamageAction::new);
	public static final RegistryObject<EquippedItemAction> EQUIPPED_ITEM_ACTION = ENTITY_ACTIONS.register("equipped_item_action", EquippedItemAction::new);
	public static final RegistryObject<FeedAction> FEED = ENTITY_ACTIONS.register("feed", FeedAction::new);
	public static final RegistryObject<GiveAction> GIVE = ENTITY_ACTIONS.register("give", GiveAction::new);
	public static final RegistryObject<PlaySoundAction> PLAY_SOUND = ENTITY_ACTIONS.register("play_sound", PlaySoundAction::new);
	public static final RegistryObject<SpawnEffectCloudAction> SPAWN_EFFECT_CLOUD = ENTITY_ACTIONS.register("spawn_effect_cloud", SpawnEffectCloudAction::new);
	public static final RegistryObject<SpawnEntityAction> SPAWN_ENTITY = ENTITY_ACTIONS.register("spawn_entity", SpawnEntityAction::new);
	public static final RegistryObject<TriggerCooldownAction> TRIGGER_COOLDOWN = ENTITY_ACTIONS.register("trigger_cooldown", TriggerCooldownAction::new);

	public static final RegistryObject<ToggleAction> TOGGLE = ENTITY_ACTIONS.register("toggle", ToggleAction::new);
	public static final RegistryObject<EmitGameEventAction> EMIT_GAME_EVENT = ENTITY_ACTIONS.register("emit_game_event", EmitGameEventAction::new);
	public static final RegistryObject<ChangeResourceAction> SET_RESOURCE = ENTITY_ACTIONS.register("set_resource", () -> new ChangeResourceAction(ChangeResourceConfiguration.SET_CODEC));
	public static final RegistryObject<GrantPowerAction> GRANT_POWER = ENTITY_ACTIONS.register("grant_power", GrantPowerAction::new);
	public static final RegistryObject<RevokePowerAction> REVOKE_POWER = ENTITY_ACTIONS.register("revoke_power", RevokePowerAction::new);
	public static final RegistryObject<ExplodeAction> EXPLODE = ENTITY_ACTIONS.register("explode", ExplodeAction::new);
	public static final RegistryObject<SimpleEntityAction> DISMOUNT = ENTITY_ACTIONS.register("dismount", () -> new SimpleEntityAction(Entity::stopRiding));
	public static final RegistryObject<PassengerAction> PASSENGER_ACTION = ENTITY_ACTIONS.register("passenger_action", PassengerAction::new);
	public static final RegistryObject<RidingAction> RIDING_ACTION = ENTITY_ACTIONS.register("riding_action", RidingAction::new);
	public static final RegistryObject<AreaOfEffectAction> AREA_OF_EFFECT = ENTITY_ACTIONS.register("area_of_effect", AreaOfEffectAction::new);
	public static final RegistryObject<CraftingTableAction> CRAFTING_TABLE = ENTITY_ACTIONS.register("crafting_table", CraftingTableAction::new);
	public static final RegistryObject<EnderChestAction> ENDER_CHEST = ENTITY_ACTIONS.register("ender_chest", EnderChestAction::new);
	public static final RegistryObject<SwingHandAction> SWING_HAND = ENTITY_ACTIONS.register("swing_hand", SwingHandAction::new);
	public static final RegistryObject<RaycastAction> RAYCAST = ENTITY_ACTIONS.register("raycast", RaycastAction::new);
	public static final RegistryObject<SpawnParticlesAction> SPAWN_PARTICLES = ENTITY_ACTIONS.register("spawn_particles", SpawnParticlesAction::new);

	public static void bootstrap() {
		MetaFactories.defineMetaActions(ENTITY_ACTIONS, DelegatedEntityAction::new, ConfiguredEntityAction.CODEC, ConfiguredEntityCondition.CODEC, EXECUTOR, PREDICATE);
	}
}
