package dev.experimental.apoli.api.generator;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import io.github.apace100.calio.data.JsonDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public abstract class PowerGenerator extends JsonDataProvider<ConfiguredPower<?, ?>> {

	protected PowerGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper, "powers", PackType.SERVER_DATA);
	}

	@Override
	protected JsonElement asJson(ConfiguredPower<?, ?> input) {
		return ConfiguredPower.CODEC.encodeStart(JsonOps.INSTANCE, input).getOrThrow(true, s -> {});
	}

	@Override
	public @NotNull String getName() {
		return "Power Generator: " + this.modid;
	}
}
