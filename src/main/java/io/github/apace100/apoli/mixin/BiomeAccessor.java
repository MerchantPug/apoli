package io.github.apace100.apoli.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @deprecated Deprecated in favor of {@link Biome#getBiomeCategory(Holder)}
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
@Mixin(Biome.class)
public interface BiomeAccessor {

    @Accessor("biomeCategory")
    Biome.BiomeCategory getCategory();
}
