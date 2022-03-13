package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record EntityGlowConfiguration(@Nullable ConfiguredEntityCondition<?, ?> entityCondition,
									  @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition, boolean useTeams,
									  ColorConfiguration color) implements IDynamicFeatureConfiguration {
	public EntityGlowConfiguration(@Nullable ConfiguredEntityCondition<?, ?> entityCondition) {
		this(entityCondition, null, true, ColorConfiguration.DEFAULT);
	}

	public static final Codec<EntityGlowConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				CalioCodecHelper.optionalField(ConfiguredEntityCondition.CODEC, "entity_condition").forGetter(x -> Optional.ofNullable(x.entityCondition())),
				CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition").forGetter(x -> Optional.ofNullable(x.biEntityCondition())),
				CalioCodecHelper.optionalField(Codec.BOOL, "use_teams", true).forGetter(EntityGlowConfiguration::useTeams),
				ColorConfiguration.NO_ALPHA.forGetter(EntityGlowConfiguration::color)
		).apply(instance, (t1, t2, t3, t4) -> new EntityGlowConfiguration(t1.orElse(null), t2.orElse(null), t3, t4)));

	public boolean applyChecks(Entity actor, Entity target, boolean targetSelf) {
		return ConfiguredEntityCondition.check(this.entityCondition(), target) && ConfiguredBiEntityCondition.check(this.biEntityCondition(), targetSelf? target : actor, targetSelf ? actor : target);
	}
}
