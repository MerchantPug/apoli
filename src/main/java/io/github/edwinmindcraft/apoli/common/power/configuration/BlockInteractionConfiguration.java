package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.InteractionPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

public record BlockInteractionConfiguration(@Nullable ConfiguredBlockCondition<?, ?> blockCondition,
											@Nullable ConfiguredBlockAction<?, ?> blockAction,
											EnumSet<Direction> directions,
											@Nullable ConfiguredEntityAction<?, ?> entityAction,
											InteractionPowerConfiguration interaction) implements IDynamicFeatureConfiguration {

	public static final Codec<BlockInteractionConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition").forGetter(x -> Optional.ofNullable(x.blockCondition())),
			CalioCodecHelper.optionalField(ConfiguredBlockAction.CODEC, "block_action").forGetter(x -> Optional.ofNullable(x.blockAction())),
			CalioCodecHelper.<EnumSet<Direction>>optionalField(SerializableDataTypes.DIRECTION_SET, "directions", () -> EnumSet.allOf(Direction.class)).forGetter(BlockInteractionConfiguration::directions),
			CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action").forGetter(x -> Optional.ofNullable(x.entityAction())),
			InteractionPowerConfiguration.MAP_CODEC.forGetter(BlockInteractionConfiguration::interaction)
	).apply(instance, (t1, t2, t3, t4, t5) -> new BlockInteractionConfiguration(t1.orElse(null), t2.orElse(null), t3, t4.orElse(null), t5)));




	public boolean check(Level level, BlockPos blockPos, Direction direction, InteractionHand hand, ItemStack heldStack) {
		if(!this.interaction().appliesTo(level, hand, heldStack))
			return false;
		if(!this.directions().contains(direction)) {
			return false;
		}
		return ConfiguredBlockCondition.check(this.blockCondition(), level, blockPos);
	}

	public InteractionResult executeAction(Entity entity, BlockPos blockPos, Direction direction, InteractionHand hand) {
		ConfiguredBlockAction.execute(this.blockAction(), entity.level, blockPos, direction);
		ConfiguredEntityAction.execute(this.entityAction(), entity);
		if (entity instanceof LivingEntity living)
			this.interaction().performActorItemStuff(living, hand);
		return this.interaction().actionResult();
	}
}
