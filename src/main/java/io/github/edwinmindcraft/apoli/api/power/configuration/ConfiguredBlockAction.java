package io.github.edwinmindcraft.apoli.api.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.ConfiguredFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public final class ConfiguredBlockAction<C extends IDynamicFeatureConfiguration, F extends BlockAction<C>> extends ConfiguredFactory<C, F> {
	public static final Codec<ConfiguredBlockAction<?, ?>> CODEC = BlockAction.CODEC.dispatch(ConfiguredBlockAction::getFactory, BlockAction::getCodec);

	public ConfiguredBlockAction(F factory, C configuration) {
		super(factory, configuration);
	}

	public void execute(Level world, BlockPos pos, Direction direction) {
		this.getFactory().execute(this.getConfiguration(), world, pos, direction);
	}


	@Override
	public String toString() {
		return "CBA:" + this.getFactory().getRegistryName() + "-" + this.getConfiguration();
	}
}
