package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BiEntityConditionConfiguration(Holder<ConfiguredBiEntityCondition<?,?>> biEntityCondition) implements IDynamicFeatureConfiguration {
	public static final Codec<BiEntityConditionConfiguration> CODEC = ConfiguredBiEntityCondition.optional("bientity_condition")
			.xmap(BiEntityConditionConfiguration::new, BiEntityConditionConfiguration::biEntityCondition).codec();
}
