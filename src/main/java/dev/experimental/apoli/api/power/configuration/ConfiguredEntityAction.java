package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.EntityAction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.entity.Entity;

public final class ConfiguredEntityAction<C extends IDynamicFeatureConfiguration, F extends EntityAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredEntityAction<?, ?>> CODEC = EntityAction.CODEC.dispatch(ConfiguredEntityAction::getFactory, Function.identity());

	public static void execute(@Nullable ConfiguredEntityAction<?, ?> action, Entity entity) {
		if (action != null)
			action.execute(entity);
	}

	public ConfiguredEntityAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(Entity entity) {
		this.getFactory().execute(this.getConfiguration(), entity);
	}
}
