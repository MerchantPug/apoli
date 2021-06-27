package dev.experimental.apoli.api.power.factory;

import com.mojang.serialization.Codec;
import dev.architectury.core.RegistryEntry;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import dev.experimental.apoli.api.power.IFactory;
import dev.experimental.apoli.api.power.configuration.ConfiguredBlockAction;
import dev.experimental.apoli.api.registry.ApoliRegistries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class BlockAction<T extends IDynamicFeatureConfiguration> extends RegistryEntry<BlockAction<?>> implements IFactory<T, ConfiguredBlockAction<T, ?>, BlockAction<T>> {
	public static final Codec<BlockAction<?>> CODEC = ApoliRegistries.codec(ApoliRegistries.BLOCK_ACTION);

	private final Codec<T> codec;

	protected BlockAction(Codec<T> codec) {
		this.codec = codec;
	}

	@Override
	public Codec<T> getCodec() {
		return codec;
	}

	@Override
	public final ConfiguredBlockAction<T, ?> configure(T input) {
		return new ConfiguredBlockAction<>(this, input);
	}

	public abstract void execute(T configuration, World world, BlockPos pos, Direction direction);
}
