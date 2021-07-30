package io.github.apace100.apoli.power;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public class PowerTypeRegistry {
    private static HashMap<ResourceLocation, PowerType> idToPower = new HashMap<>();

    public static PowerType register(ResourceLocation id, PowerType powerType) {
        if(idToPower.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate power type id tried to register: '" + id.toString() + "'");
        }
        idToPower.put(id, powerType);
        return powerType;
    }

    protected static PowerType update(ResourceLocation id, PowerType powerType) {
        if(idToPower.containsKey(id)) {
            PowerType old = idToPower.get(id);
            idToPower.remove(id);
        }
        return register(id, powerType);
    }

    public static int size() {
        return idToPower.size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return idToPower.keySet().stream();
    }

    public static Iterable<Map.Entry<ResourceLocation, PowerType>> entries() {
        return idToPower.entrySet();
    }

    public static Iterable<PowerType> values() {
        return idToPower.values();
    }

    public static PowerType get(ResourceLocation id) {
        if(!idToPower.containsKey(id)) {
            throw new IllegalArgumentException("Could not get power type from id '" + id.toString() + "', as it was not registered!");
        }
        PowerType powerType = idToPower.get(id);
        return powerType;
    }

    public static ResourceLocation getId(PowerType<?> powerType) {
        return powerType.getIdentifier();
    }

    public static boolean contains(ResourceLocation id) {
        return idToPower.containsKey(id);
    }

    public static void clear() {
        idToPower.clear();
    }

    public static void reset() {
        clear();
    }
}
