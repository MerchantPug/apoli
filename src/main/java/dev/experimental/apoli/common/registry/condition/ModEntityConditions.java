package dev.experimental.apoli.common.registry.condition;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.EnvExecutor;
import dev.experimental.apoli.api.MetaFactories;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredItemCondition;
import dev.experimental.apoli.api.power.factory.EntityCondition;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import dev.experimental.apoli.common.condition.entity.*;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.MovingEntity;
import io.github.apace100.apoli.mixin.EntityAccessor;
import io.github.apace100.calio.data.SerializableDataTypes;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.Fluid;

public class ModEntityConditions {
	public static final BiPredicate<ConfiguredEntityCondition<?, ?>, LivingEntity> PREDICATE = (config, entity) -> config.check(entity);

	public static final RegistrySupplier<SimpleEntityCondition> DAYTIME = register("daytime", entity -> entity.level.getDayTime() % 24000L < 13000L);
	public static final RegistrySupplier<SimpleEntityCondition> FALL_FLYING = register("fall_flying", LivingEntity::isFallFlying);
	public static final RegistrySupplier<SimpleEntityCondition> EXPOSED_TO_SUN = register("exposed_to_sun", entity -> entity.getBrightness() > 0.5F && SimpleEntityCondition.isExposedToSky(entity));
	public static final RegistrySupplier<SimpleEntityCondition> IN_RAIN = register("in_rain", x -> ((EntityAccessor) x).callIsBeingRainedOn());
	public static final RegistrySupplier<SimpleEntityCondition> INVISIBLE = register("invisible", Entity::isInvisible);
	public static final RegistrySupplier<SimpleEntityCondition> ON_FIRE = register("on_fire", Entity::isOnFire);
	public static final RegistrySupplier<SimpleEntityCondition> EXPOSED_TO_SKY = register("exposed_to_sky", SimpleEntityCondition::isExposedToSky);
	public static final RegistrySupplier<SimpleEntityCondition> SNEAKING = register("sneaking", Entity::isShiftKeyDown);
	public static final RegistrySupplier<SimpleEntityCondition> SPRINTING = register("sprinting", Entity::isSprinting);
	public static final RegistrySupplier<SimpleEntityCondition> SWIMMING = register("swimming", Entity::isSwimming);
	public static final RegistrySupplier<SimpleEntityCondition> COLLIDED_HORIZONTALLY = register("collided_horizontally", t -> t.horizontalCollision);
	public static final RegistrySupplier<SimpleEntityCondition> CLIMBING = register("climbing", LivingEntity::onClimbable);
	public static final RegistrySupplier<SimpleEntityCondition> TAMED = register("tamed", x -> x instanceof TameableEntity te && te.isTamed());
	public static final RegistrySupplier<SimpleEntityCondition> MOVING = register("moving", x -> ((MovingEntity) x).isMoving());
	public static final RegistrySupplier<FloatComparingEntityCondition> BRIGHTNESS = registerFloat("brightness", Entity::getBrightness);
	public static final RegistrySupplier<FloatComparingEntityCondition> SATURATION_LEVEL = registerFloat("saturation_level", x -> x instanceof Player ? ((Player) x).getFoodData().getSaturationLevel() : null);
	public static final RegistrySupplier<FloatComparingEntityCondition> HEALTH = registerFloat("health", LivingEntity::getHealth);
	public static final RegistrySupplier<FloatComparingEntityCondition> RELATIVE_HEALTH = registerFloat("relative_health", t -> t.getHealth() / t.getMaxHealth());
	public static final RegistrySupplier<FloatComparingEntityCondition> FALL_DISTANCE = registerFloat("fall_distance", t -> t.fallDistance);
	public static final RegistrySupplier<IntComparingEntityCondition> TIME_OF_DAY = registerInt("time_of_day", t -> Math.toIntExact(t.level.getDayTime() % 24000L));
	public static final RegistrySupplier<IntComparingEntityCondition> AIR = registerInt("air", Entity::getAirSupply);
	public static final RegistrySupplier<IntComparingEntityCondition> FOOD_LEVEL = registerIntPlayer("food_level", x -> x.getFoodData().getFoodLevel());
	public static final RegistrySupplier<IntComparingEntityCondition> XP_LEVELS = registerIntPlayer("xp_levels", x -> x.experienceLevel);
	public static final RegistrySupplier<IntComparingEntityCondition> XP_POINTS = registerIntPlayer("xp_points", x -> x.totalExperience);
	public static final RegistrySupplier<EnchantmentCondition> ENCHANTMENT = register("enchantment", EnchantmentCondition::new);
	public static final RegistrySupplier<BlockCollisionCondition> BLOCK_COLLISION = register("block_collision", BlockCollisionCondition::new);
	public static final RegistrySupplier<PowerActiveCondition> POWER_ACTIVE = register("power_active", PowerActiveCondition::new);
	public static final RegistrySupplier<StatusEffectCondition> STATUS_EFFECT = register("status_effect", StatusEffectCondition::new);
	public static final RegistrySupplier<SingleFieldEntityCondition<Tag<Fluid>>> SUBMERGED_IN = register("submerged_in", SerializableDataTypes.FLUID_TAG.fieldOf("fluid"), Entity::isEyeInFluid);
	public static final RegistrySupplier<SingleFieldEntityCondition<EntityType<?>>> ENTITY_TYPE = register("entity_type", SerializableDataTypes.ENTITY_TYPE.fieldOf("entity_type"), (entity, o) -> Objects.equals(o, entity.getType()));
	public static final RegistrySupplier<SingleFieldEntityCondition<Tag<EntityType<?>>>> IN_TAG = register("in_tag", SerializableDataTypes.ENTITY_TAG.fieldOf("tag"), (entity, o) -> entity.getType().isIn(o));
	public static final RegistrySupplier<PowerCondition> POWER = register("power", PowerCondition::new);
	public static final RegistrySupplier<FluidHeightCondition> FLUID_HEIGHT = register("fluid_height", FluidHeightCondition::new);
	public static final RegistrySupplier<SingleFieldEntityCondition<Optional<ConfiguredBlockCondition<?, ?>>>> ON_BLOCK = register("on_block", ConfiguredBlockCondition.CODEC.optionalFieldOf("block_condition"), (entity, configuration) -> entity.isOnGround() && configuration.map(x -> x.check(new BlockInWorld(entity.level, entity.blockPosition(), true))).orElse(true));
	public static final RegistrySupplier<SingleFieldEntityCondition<ConfiguredBlockCondition<?, ?>>> IN_BLOCK = register("in_block", ConfiguredBlockCondition.CODEC.fieldOf("block_condition"), (entity, configuration) -> configuration.check(new BlockInWorld(entity.level, entity.blockPosition(), true)));
	public static final RegistrySupplier<ResourceCondition> RESOURCE = register("resource", ResourceCondition::new);
	public static final RegistrySupplier<SingleFieldEntityCondition<ResourceKey<Level>>> DIMENSION = register("dimension", SerializableDataTypes.DIMENSION.fieldOf("dimension"), (entity, dimension) -> entity.getEntityWorld().getRegistryKey().equals(dimension));
	public static final RegistrySupplier<SingleFieldEntityCondition<MobType>> ENTITY_GROUP = register("entity_group", SerializableDataTypes.ENTITY_GROUP.fieldOf("group"), (entity, group) -> entity.getGroup().equals(group));
	public static final RegistrySupplier<SingleFieldEntityCondition<Optional<ConfiguredItemCondition<?, ?>>>> USING_ITEM = register("using_item", ConfiguredItemCondition.CODEC.optionalFieldOf("item_condition"), (entity, configuration) -> entity.isUsingItem() && configuration.map(x -> x.check(entity.getItemInHand(entity.getUsedItemHand()))).orElse(true));
	public static final RegistrySupplier<SingleFieldEntityCondition<ResourceLocation>> PREDICATE_CONDITION = register("predicate", ResourceLocation.CODEC.fieldOf("predicate"), SingleFieldEntityCondition::checkPredicate);
	public static final RegistrySupplier<EquippedItemCondition> EQUIPPED_ITEM = register("equipped_item", EquippedItemCondition::new);
	public static final RegistrySupplier<CommandCondition> COMMAND = register("command", CommandCondition::new);
	public static final RegistrySupplier<AttributeCondition> ATTRIBUTE = register("attribute", AttributeCondition::new);
	public static final RegistrySupplier<BlockInRadiusCondition> BLOCK_IN_RADIUS = register("block_in_radius", BlockInRadiusCondition::new);
	public static final RegistrySupplier<BiomeCondition> BIOME = register("biome", BiomeCondition::new);
	public static final RegistrySupplier<ScoreboardCondition> SCOREBOARD = register("scoreboard", ScoreboardCondition::new);
	public static final RegistrySupplier<InBlockAnywhereCondition> IN_BLOCK_ANYWHERE = register("in_block_anywhere", InBlockAnywhereCondition::new);
	public static final RegistrySupplier<UsingEffectiveToolCondition> USING_EFFECTIVE_TOOL = registerSided("using_effective_tool", () -> UsingEffectiveToolCondition::new, () -> UsingEffectiveToolCondition.Client::new);
	public static final RegistrySupplier<AdvancementCondition> ADVANCEMENT = registerSided("advancement", () -> AdvancementCondition::new, () -> AdvancementCondition.Client::new);
	public static final RegistrySupplier<GameModeCondition> GAMEMODE = registerSided("gamemode", () -> GameModeCondition::new, () -> GameModeCondition.Client::new);

	public static void register() {
		MetaFactories.defineMetaConditions(ApoliRegistries.ENTITY_CONDITION, DelegatedEntityCondition::new, ConfiguredEntityCondition.CODEC, PREDICATE);
	}

	private static <T extends EntityCondition<?>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
		return ApoliRegistries.ENTITY_CONDITION.register(Apoli.identifier(name), factory);
	}

	private static RegistrySupplier<SimpleEntityCondition> register(String name, Predicate<LivingEntity> factory) {
		return register(name, () -> new SimpleEntityCondition(factory));
	}

	private static RegistrySupplier<IntComparingEntityCondition> registerInt(String name, Function<LivingEntity, Integer> factory) {
		return register(name, () -> new IntComparingEntityCondition(factory));
	}

	private static RegistrySupplier<IntComparingEntityCondition> registerIntPlayer(String name, Function<Player, Integer> factory) {
		return registerInt(name, living -> living instanceof PlayerEntity pe ? factory.apply(pe) : null);
	}

	private static RegistrySupplier<FloatComparingEntityCondition> registerFloat(String name, Function<LivingEntity, Float> factory) {
		return register(name, () -> new FloatComparingEntityCondition(factory));
	}

	private static <T> RegistrySupplier<SingleFieldEntityCondition<T>> register(String name, MapCodec<T> codec, BiPredicate<LivingEntity, T> predicate) {
		return register(name, () -> new SingleFieldEntityCondition<>(codec, predicate));
	}

	private static <T extends EntityCondition<?>> RegistrySupplier<T> registerSided(String name, Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
		return register(name, () -> EnvExecutor.getEnvSpecific(client, server));
	}
}
