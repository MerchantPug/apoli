package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class ConfiguredBiEntityAction<C extends IDynamicFeatureConfiguration, F extends BiEntityAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredBiEntityAction<?, ?>> CODEC = BiEntityAction.CODEC.dispatch(ConfiguredBiEntityAction::getFactory, BiEntityAction::getCodec);

	public static void execute(@Nullable ConfiguredBiEntityAction<?, ?> action, Entity actor, Entity target) {
		if (action != null)
			action.execute(actor, target);
	}

	public ConfiguredBiEntityAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(Entity actor, Entity target) {
		this.getFactory().execute(this.getConfiguration(), actor, target);
	}

	@Override
	public String toString() {
		return "CEA:" + this.getFactory().getRegistryName() + "-" + this.getConfiguration();
	}
}
