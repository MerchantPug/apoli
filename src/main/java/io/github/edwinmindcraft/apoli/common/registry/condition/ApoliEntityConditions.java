package io.github.edwinmindcraft.apoli.common.registry.condition;

import com.mojang.serialization.MapCodec;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.MovingEntity;
import io.github.apace100.apoli.mixin.EntityAccessor;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.MetaFactories;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.entity.*;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConstantConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ApoliEntityConditions {
	public static final BiPredicate<ConfiguredEntityCondition<?, ?>, Entity> PREDICATE = (config, entity) -> config.check(entity);

	private static <U extends EntityCondition<?>> RegistryObject<U> of(String name) {
		return RegistryObject.of(Apoli.identifier(name), ApoliRegistries.ENTITY_CONDITION_CLASS, Apoli.MODID);
	}

	public static final RegistryObject<DelegatedEntityCondition<ConstantConfiguration<Entity>>> CONSTANT = of("constant");
	public static final RegistryObject<DelegatedEntityCondition<ConditionStreamConfiguration<ConfiguredEntityCondition<?, ?>, Entity>>> AND = of("and");
	public static final RegistryObject<DelegatedEntityCondition<ConditionStreamConfiguration<ConfiguredEntityCondition<?, ?>, Entity>>> OR = of("or");

	public static final RegistryObject<SimpleEntityCondition> DAYTIME = register("daytime", entity -> entity.level.getDayTime() % 24000L < 13000L);
	public static final RegistryObject<SimpleEntityCondition> FALL_FLYING = registerLiving("fall_flying", LivingEntity::isFallFlying);
	public static final RegistryObject<SimpleEntityCondition> EXPOSED_TO_SUN = register("exposed_to_sun", entity -> entity.getBrightness() > 0.5F && SimpleEntityCondition.isExposedToSky(entity));
	public static final RegistryObject<SimpleEntityCondition> IN_RAIN = register("in_rain", x -> ((EntityAccessor) x).callIsBeingRainedOn());
	public static final RegistryObject<SimpleEntityCondition> INVISIBLE = register("invisible", Entity::isInvisible);
	public static final RegistryObject<SimpleEntityCondition> ON_FIRE = register("on_fire", Entity::isOnFire);
	public static final RegistryObject<SimpleEntityCondition> EXPOSED_TO_SKY = register("exposed_to_sky", SimpleEntityCondition::isExposedToSky);
	public static final RegistryObject<SimpleEntityCondition> SNEAKING = register("sneaking", Entity::isShiftKeyDown);
	public static final RegistryObject<SimpleEntityCondition> SPRINTING = register("sprinting", Entity::isSprinting);
	public static final RegistryObject<SimpleEntityCondition> SWIMMING = register("swimming", Entity::isSwimming);
	public static final RegistryObject<SimpleEntityCondition> COLLIDED_HORIZONTALLY = register("collided_horizontally", t -> t.horizontalCollision);
	public static final RegistryObject<SimpleEntityCondition> CLIMBING = registerLiving("climbing", LivingEntity::onClimbable);
	public static final RegistryObject<SimpleEntityCondition> TAMED = register("tamed", x -> x instanceof TamableAnimal te && te.isTame());
	public static final RegistryObject<SimpleEntityCondition> MOVING = register("moving", x -> ((MovingEntity) x).isMoving());
	public static final RegistryObject<FloatComparingEntityCondition> BRIGHTNESS = registerFloat("brightness", Entity::getBrightness);
	public static final RegistryObject<FloatComparingEntityCondition> SATURATION_LEVEL = registerFloat("saturation_level", x -> x instanceof Player ? ((Player) x).getFoodData().getSaturationLevel() : null);
	public static final RegistryObject<FloatComparingEntityCondition> HEALTH = registerFloatLiving("health", LivingEntity::getHealth);
	public static final RegistryObject<FloatComparingEntityCondition> RELATIVE_HEALTH = registerFloatLiving("relative_health", t -> t.getHealth() / t.getMaxHealth());
	public static final RegistryObject<FloatComparingEntityCondition> FALL_DISTANCE = registerFloat("fall_distance", t -> t.fallDistance);
	public static final RegistryObject<IntComparingEntityCondition> TIME_OF_DAY = registerInt("time_of_day", t -> Math.toIntExact(t.level.getDayTime() % 24000L));
	public static final RegistryObject<IntComparingEntityCondition> AIR = registerInt("air", Entity::getAirSupply);
	public static final RegistryObject<IntComparingEntityCondition> FOOD_LEVEL = registerIntPlayer("food_level", x -> x.getFoodData().getFoodLevel());
	public static final RegistryObject<IntComparingEntityCondition> XP_LEVELS = registerIntPlayer("xp_levels", x -> x.experienceLevel);
	public static final RegistryObject<IntComparingEntityCondition> XP_POINTS = registerIntPlayer("xp_points", x -> x.totalExperience);
	public static final RegistryObject<EnchantmentCondition> ENCHANTMENT = register("enchantment", EnchantmentCondition::new);
	public static final RegistryObject<BlockCollisionCondition> BLOCK_COLLISION = register("block_collision", BlockCollisionCondition::new);
	public static final RegistryObject<PowerActiveCondition> POWER_ACTIVE = register("power_active", PowerActiveCondition::new);
	public static final RegistryObject<StatusEffectCondition> STATUS_EFFECT = register("status_effect", StatusEffectCondition::new);
	public static final RegistryObject<SubmergedInCondition> SUBMERGED_IN = register("submerged_in", SubmergedInCondition::new);
	public static final RegistryObject<SingleFieldEntityCondition<EntityType<?>>> ENTITY_TYPE = register("entity_type", SerializableDataTypes.ENTITY_TYPE.fieldOf("entity_type"), (entity, o) -> Objects.equals(o, entity.getType()));
	public static final RegistryObject<InTagCondition> IN_TAG = register("in_tag", InTagCondition::new);
	public static final RegistryObject<PowerCondition> POWER = register("power", PowerCondition::new);
	public static final RegistryObject<FluidHeightCondition> FLUID_HEIGHT = register("fluid_height", FluidHeightCondition::new);
	public static final RegistryObject<SingleFieldEntityCondition<Optional<ConfiguredBlockCondition<?, ?>>>> ON_BLOCK = register("on_block", CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition"), (entity, configuration) -> entity.isOnGround() && configuration.map(x -> x.check(new BlockInWorld(entity.level, entity.blockPosition(), true))).orElse(true));
	public static final RegistryObject<SingleFieldEntityCondition<ConfiguredBlockCondition<?, ?>>> IN_BLOCK = register("in_block", ConfiguredBlockCondition.CODEC.fieldOf("block_condition"), (entity, configuration) -> configuration.check(new BlockInWorld(entity.level, entity.blockPosition(), true)));
	public static final RegistryObject<ResourceCondition> RESOURCE = register("resource", ResourceCondition::new);
	public static final RegistryObject<SingleFieldEntityCondition<ResourceKey<Level>>> DIMENSION = register("dimension", SerializableDataTypes.DIMENSION.fieldOf("dimension"), (entity, dimension) -> entity.getCommandSenderWorld().dimension().equals(dimension));
	public static final RegistryObject<SingleFieldEntityCondition<MobType>> ENTITY_GROUP = register("entity_group", SerializableDataTypes.ENTITY_GROUP.fieldOf("group"), (entity, group) -> entity instanceof LivingEntity living && living.getMobType().equals(group));
	public static final RegistryObject<SingleFieldEntityCondition<Optional<ConfiguredItemCondition<?, ?>>>> USING_ITEM = register("using_item", CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition"), (entity, configuration) -> entity instanceof LivingEntity living && living.isUsingItem() && configuration.map(x -> x.check(living.level, living.getItemInHand(living.getUsedItemHand()))).orElse(true));
	public static final RegistryObject<SingleFieldEntityCondition<ResourceLocation>> PREDICATE_CONDITION = register("predicate", ResourceLocation.CODEC.fieldOf("predicate"), SingleFieldEntityCondition::checkPredicate);
	public static final RegistryObject<EquippedItemCondition> EQUIPPED_ITEM = register("equipped_item", EquippedItemCondition::new);
	public static final RegistryObject<CommandCondition> COMMAND = register("command", CommandCondition::new);
	public static final RegistryObject<AttributeCondition> ATTRIBUTE = register("attribute", AttributeCondition::new);
	public static final RegistryObject<BlockInRadiusCondition> BLOCK_IN_RADIUS = register("block_in_radius", BlockInRadiusCondition::new);
	public static final RegistryObject<BiomeCondition> BIOME = register("biome", BiomeCondition::new);
	public static final RegistryObject<ScoreboardCondition> SCOREBOARD = register("scoreboard", ScoreboardCondition::new);
	public static final RegistryObject<InBlockAnywhereCondition> IN_BLOCK_ANYWHERE = register("in_block_anywhere", InBlockAnywhereCondition::new);
	public static final RegistryObject<UsingEffectiveToolCondition> USING_EFFECTIVE_TOOL = registerSided("using_effective_tool", () -> UsingEffectiveToolCondition.Client::new, () -> UsingEffectiveToolCondition::new);
	public static final RegistryObject<AdvancementCondition> ADVANCEMENT = registerSided("advancement", () -> AdvancementCondition.Client::new, () -> AdvancementCondition::new);
	public static final RegistryObject<GameModeCondition> GAMEMODE = registerSided("gamemode", () -> GameModeCondition.Client::new, () -> GameModeCondition::new);

	public static ConfiguredEntityCondition<?, ?> constant(boolean value) {return CONSTANT.get().configure(new ConstantConfiguration<>(value));}

	public static ConfiguredEntityCondition<?, ?> and(ConfiguredEntityCondition<?, ?>... conditions) {return AND.get().configure(ConditionStreamConfiguration.and(Arrays.asList(conditions), PREDICATE));}

	public static ConfiguredEntityCondition<?, ?> or(ConfiguredEntityCondition<?, ?>... conditions) {return OR.get().configure(ConditionStreamConfiguration.or(Arrays.asList(conditions), PREDICATE));}

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegisters.ENTITY_CONDITIONS, DelegatedEntityCondition::new, ConfiguredEntityCondition.CODEC, PREDICATE);
	}

	private static <T extends EntityCondition<?>> RegistryObject<T> register(String name, Supplier<T> factory) {
		return ApoliRegisters.ENTITY_CONDITIONS.register(name, factory);
	}

	private static RegistryObject<SimpleEntityCondition> register(String name, Predicate<Entity> factory) {
		return register(name, () -> new SimpleEntityCondition(factory));
	}

	private static RegistryObject<SimpleEntityCondition> registerLiving(String name, Predicate<LivingEntity> factory) {
		return register(name, () -> new SimpleEntityCondition(x -> x instanceof LivingEntity living && factory.test(living)));
	}

	private static RegistryObject<IntComparingEntityCondition> registerInt(String name, Function<Entity, Integer> factory) {
		return register(name, () -> new IntComparingEntityCondition(factory));
	}

	private static RegistryObject<IntComparingEntityCondition> registerIntPlayer(String name, Function<Player, Integer> factory) {
		return registerInt(name, living -> living instanceof Player pe ? factory.apply(pe) : null);
	}

	private static RegistryObject<FloatComparingEntityCondition> registerFloat(String name, Function<Entity, Float> factory) {
		return register(name, () -> new FloatComparingEntityCondition(factory));
	}

	private static RegistryObject<FloatComparingEntityCondition> registerFloatLiving(String name, Function<LivingEntity, Float> factory) {
		return register(name, () -> new FloatComparingEntityCondition(x -> x instanceof LivingEntity living ? factory.apply(living) : null));
	}

	private static <T> RegistryObject<SingleFieldEntityCondition<T>> register(String name, MapCodec<T> codec, BiPredicate<Entity, T> predicate) {
		return register(name, () -> new SingleFieldEntityCondition<>(codec, predicate));
	}

	private static <T extends EntityCondition<?>> RegistryObject<T> registerSided(String name, Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
		return register(name, () -> DistExecutor.unsafeRunForDist(client, server));
	}
}
