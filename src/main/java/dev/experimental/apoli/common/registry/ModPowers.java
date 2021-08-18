package dev.experimental.apoli.common.registry;

import dev.experimental.apoli.common.power.*;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraftforge.fmllegacy.RegistryObject;

import static dev.experimental.apoli.common.registry.ApoliRegisters.POWER_FACTORIES;

public class ModPowers {
	public static final RegistryObject<ActionOnBlockBreakPower> ACTION_ON_BLOCK_BREAK = POWER_FACTORIES.register("action_on_block_break", ActionOnBlockBreakPower::new);
	public static final RegistryObject<ActionOnCallbackPower> ACTION_ON_CALLBACK = POWER_FACTORIES.register("action_on_callback", ActionOnCallbackPower::new);
	public static final RegistryObject<ActionOnItemUsePower> ACTION_ON_ITEM_USE = POWER_FACTORIES.register("action_on_item_use", ActionOnItemUsePower::new);
	public static final RegistryObject<ActionOnLandPower> ACTION_ON_LAND = POWER_FACTORIES.register("action_on_land", ActionOnLandPower::new);
	public static final RegistryObject<ActionOnWakeUpPower> ACTION_ON_WAKE_UP = POWER_FACTORIES.register("action_on_wake_up", ActionOnWakeUpPower::new);
	public static final RegistryObject<ActionOverTimePower> ACTION_OVER_TIME = POWER_FACTORIES.register("action_over_time", ActionOverTimePower::new);
	public static final RegistryObject<ActiveSelfPower> ACTIVE_SELF = POWER_FACTORIES.register("active_self", ActiveSelfPower::new);
	public static final RegistryObject<AttackerActionWhenHitPower> ATTACKER_ACTION_WHEN_HIT = POWER_FACTORIES.register("attacker_action_when_hit", AttackerActionWhenHitPower::new);
	public static final RegistryObject<AttributePower> ATTRIBUTE = POWER_FACTORIES.register("attribute", AttributePower::new);
	public static final RegistryObject<BurnPower> BURN = POWER_FACTORIES.register("burn", BurnPower::new);
	public static final RegistryObject<ClimbingPower> CLIMBING = POWER_FACTORIES.register("climbing", ClimbingPower::new);
	public static final RegistryObject<ConditionedAttributePower> CONDITIONED_ATTRIBUTE = POWER_FACTORIES.register("conditioned_attribute", ConditionedAttributePower::new);
	public static final RegistryObject<ConditionedRestrictArmorPower> CONDITIONED_RESTRICT_ARMOR = POWER_FACTORIES.register("conditioned_restrict_armor", ConditionedRestrictArmorPower::new);
	public static final RegistryObject<CooldownPower> COOLDOWN = POWER_FACTORIES.register("cooldown", CooldownPower::new);
	public static final RegistryObject<CreativeFlightPower> CREATIVE_FLIGHT = POWER_FACTORIES.register("creative_flight", CreativeFlightPower::new);
	public static final RegistryObject<DamageOverTimePower> DAMAGE_OVER_TIME = POWER_FACTORIES.register("damage_over_time", DamageOverTimePower::new);
	public static final RegistryObject<DummyPower> DISABLE_REGEN = POWER_FACTORIES.register("disable_regen", DummyPower::new);
	public static final RegistryObject<EffectImmunityPower> EFFECT_IMMUNITY = POWER_FACTORIES.register("effect_immunity", EffectImmunityPower::new);
	public static final RegistryObject<ElytraFlightPower> ELYTRA_FLIGHT = POWER_FACTORIES.register("elytra_flight", ElytraFlightPower::new);
	public static final RegistryObject<EntityGlowPower> ENTITY_GLOW = POWER_FACTORIES.register("entity_glow", EntityGlowPower::new);
	public static final RegistryObject<EntityGroupPower> ENTITY_GROUP = POWER_FACTORIES.register("entity_group", EntityGroupPower::new);
	public static final RegistryObject<ExhaustOverTimePower> EXHAUST_OVER_TIME = POWER_FACTORIES.register("exhaust", ExhaustOverTimePower::new);
	public static final RegistryObject<DummyPower> FIRE_IMMUNITY = POWER_FACTORIES.register("fire_immunity", DummyPower::new);
	public static final RegistryObject<FireProjectilePower> FIRE_PROJECTILE = POWER_FACTORIES.register("fire_projectile", FireProjectilePower::new);
	public static final RegistryObject<DummyPower> IGNORE_WATER = POWER_FACTORIES.register("ignore_water", DummyPower::new);
	public static final RegistryObject<InventoryPower> INVENTORY = POWER_FACTORIES.register("inventory", () -> new InventoryPower(9, inventory -> (i, playerInv, player) -> new DispenserMenu(i, playerInv, inventory)));
	public static final RegistryObject<InvisibilityPower> INVISIBILITY = POWER_FACTORIES.register("invisibility", InvisibilityPower::new);
	public static final RegistryObject<InvulnerablePower> INVULNERABILITY = POWER_FACTORIES.register("invulnerability", InvulnerablePower::new);
	public static final RegistryObject<LaunchPower> LAUNCH = POWER_FACTORIES.register("launch", LaunchPower::new);
	public static final RegistryObject<LavaVisionPower> LAVA_VISION = POWER_FACTORIES.register("lava_vision", LavaVisionPower::new);
	public static final RegistryObject<ModelColorPower> MODEL_COLOR = POWER_FACTORIES.register("model_color", ModelColorPower::new);
	public static final RegistryObject<ModifyBreakSpeedPower> MODIFY_BREAK_SPEED = POWER_FACTORIES.register("modify_break_speed", ModifyBreakSpeedPower::new);
	public static final RegistryObject<ModifyDamageDealtPower> MODIFY_DAMAGE_DEALT = POWER_FACTORIES.register("modify_damage_dealt", ModifyDamageDealtPower::new);
	public static final RegistryObject<ModifyDamageTakenPower> MODIFY_DAMAGE_TAKEN = POWER_FACTORIES.register("modify_damage_taken", ModifyDamageTakenPower::new);
	public static final RegistryObject<ModifyValuePower> MODIFY_EXHAUSTION = POWER_FACTORIES.register("modify_exhaustion", ModifyValuePower::new);
	public static final RegistryObject<ModifyValuePower> MODIFY_EXPERIENCE = POWER_FACTORIES.register("modify_xp_gain", ModifyValuePower::new);
	public static final RegistryObject<ModifyFallingPower> MODIFY_FALLING = POWER_FACTORIES.register("modify_falling", ModifyFallingPower::new);
	public static final RegistryObject<ModifyFoodPower> MODIFY_FOOD = POWER_FACTORIES.register("modify_food", ModifyFoodPower::new);
	public static final RegistryObject<ModifyHarvestPower> MODIFY_HARVEST = POWER_FACTORIES.register("modify_harvest", ModifyHarvestPower::new);
	public static final RegistryObject<ModifyJumpPower> MODIFY_JUMP = POWER_FACTORIES.register("modify_jump", ModifyJumpPower::new);
	public static final RegistryObject<ModifyValuePower> MODIFY_LAVA_SPEED = POWER_FACTORIES.register("modify_lava_speed", ModifyValuePower::new);
	public static final RegistryObject<ModifyPlayerSpawnPower> MODIFY_PLAYER_SPAWN = POWER_FACTORIES.register("modify_player_spawn", ModifyPlayerSpawnPower::new);
	public static final RegistryObject<ModifyDamageDealtPower> MODIFY_PROJECTILE_DAMAGE = POWER_FACTORIES.register("modify_projectile_damage", ModifyDamageDealtPower::new);
	public static final RegistryObject<ModifySwimSpeedPower> MODIFY_SWIM_SPEED = POWER_FACTORIES.register("modify_swim_speed", ModifySwimSpeedPower::new);
	public static final RegistryObject<MultiplePower> MULTIPLE = POWER_FACTORIES.register("multiple", MultiplePower::new);
	public static final RegistryObject<NightVisionPower> NIGHT_VISION = POWER_FACTORIES.register("night_vision", NightVisionPower::new);
	public static final RegistryObject<ParticlePower> PARTICLE = POWER_FACTORIES.register("particle", ParticlePower::new);
	public static final RegistryObject<PhasingPower> PHASING = POWER_FACTORIES.register("phasing", PhasingPower::new);
	public static final RegistryObject<PreventBlockActionPower> PREVENT_BLOCK_SELECTION = POWER_FACTORIES.register("prevent_block_selection", PreventBlockActionPower::new);
	public static final RegistryObject<PreventBlockActionPower> PREVENT_BLOCK_USAGE = POWER_FACTORIES.register("prevent_block_use", PreventBlockActionPower::new);
	public static final RegistryObject<PreventDeathPower> PREVENT_DEATH = POWER_FACTORIES.register("prevent_death", PreventDeathPower::new);
	public static final RegistryObject<PreventEntityRenderPower> PREVENT_ENTITY_RENDER = POWER_FACTORIES.register("prevent_entity_render", PreventEntityRenderPower::new);
	public static final RegistryObject<PreventItemActionPower> PREVENT_ITEM_USAGE = POWER_FACTORIES.register("prevent_item_use", PreventItemActionPower::new);
	public static final RegistryObject<PreventSleepPower> PREVENT_SLEEP = POWER_FACTORIES.register("prevent_sleep", PreventSleepPower::new);
	public static final RegistryObject<RecipePower> RECIPE = POWER_FACTORIES.register("recipe", RecipePower::new);
	public static final RegistryObject<ResourcePower> RESOURCE = POWER_FACTORIES.register("resource", ResourcePower::new);
	public static final RegistryObject<RestrictArmorPower> RESTRICT_ARMOR = POWER_FACTORIES.register("restrict_armor", RestrictArmorPower::new);
	public static final RegistryObject<SelfCombatActionPower> SELF_ACTION_ON_HIT = POWER_FACTORIES.register("self_action_on_hit", SelfCombatActionPower::new);
	public static final RegistryObject<SelfCombatActionPower> SELF_ACTION_ON_KILL = POWER_FACTORIES.register("self_action_on_kill", SelfCombatActionPower::new);
	public static final RegistryObject<SelfActionWhenHitPower> SELF_ACTION_WHEN_HIT = POWER_FACTORIES.register("self_action_when_hit", SelfActionWhenHitPower::new);
	public static final RegistryObject<ShaderPower> SHADER = POWER_FACTORIES.register("shader", ShaderPower::new);
	public static final RegistryObject<DummyPower> SHAKING = POWER_FACTORIES.register("shaking", DummyPower::new);
	public static final RegistryObject<DummyPower> SIMPLE = POWER_FACTORIES.register("simple", DummyPower::new);
	public static final RegistryObject<StackingStatusEffectPower> STACKING_STATUS_EFFECT = POWER_FACTORIES.register("stacking_status_effect", StackingStatusEffectPower::new);
	public static final RegistryObject<StartingEquipmentPower> STARTING_EQUIPMENT = POWER_FACTORIES.register("starting_equipment", StartingEquipmentPower::new);
	public static final RegistryObject<DummyPower> SWIMMING = POWER_FACTORIES.register("swimming", DummyPower::new);
	public static final RegistryObject<TargetCombatActionPower> TARGET_ACTION_ON_HIT = POWER_FACTORIES.register("target_action_on_hit", TargetCombatActionPower::new);
	public static final RegistryObject<TogglePower> TOGGLE = POWER_FACTORIES.register("toggle", TogglePower::new);
	public static final RegistryObject<ToggleNightVisionPower> TOGGLE_NIGHT_VISION = POWER_FACTORIES.register("toggle_night_vision", ToggleNightVisionPower::new);
	public static final RegistryObject<WalkOnFluidPower> WALK_ON_FLUID = POWER_FACTORIES.register("walk_on_fluid", WalkOnFluidPower::new);

	//Those powers are, as far as I know, remains of the previous system.
	//As such, I've transformed then into actual powers.
	public static final RegistryObject<CooldownPower> WEBBING = POWER_FACTORIES.register("webbing", CooldownPower::new);
	public static final RegistryObject<DummyPower> WATER_BREATHING = POWER_FACTORIES.register("water_breathing", DummyPower::new);
	public static final RegistryObject<DummyPower> NO_COBWEB_SLOWDOWN = POWER_FACTORIES.register("no_cobweb_slowdown", DummyPower::new); //NO_COBWEB_SLOWDOWN & MASTER_OF_WEBS_NO_SLOWDOWN
	public static final RegistryObject<DummyPower> LIKE_WATER = POWER_FACTORIES.register("like_water", DummyPower::new);
	public static final RegistryObject<DummyPower> WATER_VISION = POWER_FACTORIES.register("water_vision", DummyPower::new); //TODO Might be worth transforming into a float field.
	public static final RegistryObject<DummyPower> CONDUIT_POWER_ON_LAND = POWER_FACTORIES.register("conduit_power_on_land", DummyPower::new);
	public static final RegistryObject<DummyPower> SCARE_CREEPERS = POWER_FACTORIES.register("scare_creepers", DummyPower::new);
	//public static final RegistryObject<DummyPower> AQUA_AFFINITY = POWER_FACTORIES.register("aqua_affinity", DummyPower::new);
	public static final RegistryObject<DummyPower> SLOW_FALLING = POWER_FACTORIES.register("slow_falling", DummyPower::new);

	public static void register() { }
}
