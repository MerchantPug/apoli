package io.github.apace100.apoli.power;

import com.google.gson.JsonObject;
import io.github.apace100.apoli.Apoli;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class IntegrationPowerListeners {
    public static void onPostPowerLoad(Identifier powerId, Identifier factoryId, boolean isSubPower, JsonObject json, PowerType<?> powerType) {
        if (Objects.equals(factoryId, Apoli.identifier("modify_scale")) && !FabricLoader.getInstance().isModLoaded("pehkui")) {
            Apoli.LOGGER.warn(powerId + " (which uses apoli:modify_scale) will not function. Install Pehkui in order to use this power.");
        }
    }
}
