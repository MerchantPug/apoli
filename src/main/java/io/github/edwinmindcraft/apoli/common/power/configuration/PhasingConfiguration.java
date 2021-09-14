package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PhasingConfiguration(@Nullable ConfiguredBlockCondition<?, ?> phaseCondition, boolean blacklist,
								   RenderType renderType, float viewDistance,
								   @Nullable ConfiguredEntityCondition<?, ?> phaseDownCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<PhasingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredBlockCondition.CODEC.optionalFieldOf("block_condition").forGetter(x -> Optional.ofNullable(x.phaseCondition())),
			Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(PhasingConfiguration::blacklist),
			SerializableDataType.enumValue(RenderType.class).optionalFieldOf("render_type", RenderType.BLINDNESS).forGetter(PhasingConfiguration::renderType),
			Codec.FLOAT.optionalFieldOf("view_distance", 10F).forGetter(PhasingConfiguration::viewDistance),
			ConfiguredEntityCondition.CODEC.optionalFieldOf("phase_down_condition").forGetter(x -> Optional.ofNullable(x.phaseDownCondition()))
	).apply(instance, (t1, t2, t3, t4, t5) -> new PhasingConfiguration(t1.orElse(null), t2, t3, t4, t5.orElse(null))));

	public boolean canPhaseDown(LivingEntity entity) {
		return this.phaseDownCondition() == null ? entity.isCrouching() : this.phaseDownCondition().check(entity);
	}

	public boolean canPhaseThrough(BlockInWorld cbp) {
		return this.blacklist() != ConfiguredBlockCondition.check(this.phaseCondition(), cbp);
	}

	public enum RenderType {
		BLINDNESS, REMOVE_BLOCKS
	}
}
