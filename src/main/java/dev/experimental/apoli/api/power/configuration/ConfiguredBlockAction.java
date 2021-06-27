package dev.experimental.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.ConfiguredFactory;
import dev.experimental.apoli.api.power.factory.BlockAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.function.Function;

public final class ConfiguredBlockAction<C extends IDynamicFeatureConfiguration, F extends BlockAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredBlockAction<?, ?>> CODEC = BlockAction.CODEC.dispatch(ConfiguredBlockAction::getFactory, Function.identity());

	public ConfiguredBlockAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(World world, BlockPos pos, Direction direction) {
		this.getFactory().execute(this.getConfiguration(), world, pos, direction);
	}
}
