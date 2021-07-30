package io.github.apace100.apoli.power;

import com.google.gson.*;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.NamespaceAlias;
import io.github.apace100.calio.data.MultiJsonDataLoader;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.*;
import java.util.function.BiFunction;

@SuppressWarnings("rawtypes")
public class PowerTypes extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {

    private static final ResourceLocation MULTIPLE = Apoli.identifier("multiple");
    private static final ResourceLocation SIMPLE = Apoli.identifier("simple");

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private final HashMap<ResourceLocation, Integer> loadingPriorities = new HashMap<>();

    public PowerTypes() {
        super(GSON, "powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, List<JsonElement>> loader, ResourceManager manager, ProfilerFiller profiler) {
        PowerTypeRegistry.reset();
        loadingPriorities.clear();
        loader.forEach((id, jel) -> {
            jel.forEach(je -> {
                try {
                    SerializableData.CURRENT_NAMESPACE = id.getNamespace();
                    SerializableData.CURRENT_PATH = id.getPath();
                    JsonObject jo = je.getAsJsonObject();
                    ResourceLocation factoryId = ResourceLocation.tryParse(GsonHelper.getAsString(jo, "type"));
                    if(isMultiple(factoryId)) {
                        List<ResourceLocation> subPowers = new LinkedList<>();
                        for(Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                            if( entry.getKey().equals("type")
                            ||  entry.getKey().equals("loading_priority")
                            ||  entry.getKey().equals("name")
                            ||  entry.getKey().equals("description")
                            ||  entry.getKey().equals("hidden")
                            ||  entry.getKey().equals("condition")) {
                                continue;
                            }
                            ResourceLocation subId = new ResourceLocation(id.toString() + "_" + entry.getKey());
                            try {
                                readPower(subId, entry.getValue(), true);
                                subPowers.add(subId);
                            } catch(Exception e) {
                                Apoli.LOGGER.error("There was a problem reading sub-power \"" +
                                    subId.toString() + "\" in power file \"" + id.toString() + "\": " + e.getMessage());
                            }
                        }
                        MultiplePowerType superPower = (MultiplePowerType)readPower(id, je, false, MultiplePowerType::new);
                        superPower.setSubPowers(subPowers);
                    } else {
                        readPower(id, je, false);
                    }
                } catch(Exception e) {
                    Apoli.LOGGER.error("There was a problem reading power file " + id.toString() + " (skipping): " + e.getMessage());
                }
            });
        });
        loadingPriorities.clear();
        SerializableData.CURRENT_NAMESPACE = null;
        SerializableData.CURRENT_PATH = null;
        Apoli.LOGGER.info("Finished loading powers from data files. Registry contains " + PowerTypeRegistry.size() + " powers.");
    }

    private void readPower(ResourceLocation id, JsonElement je, boolean isSubPower) {
        readPower(id, je, isSubPower, PowerType::new);
    }

    private PowerType readPower(ResourceLocation id, JsonElement je, boolean isSubPower,
                                BiFunction<ResourceLocation, PowerFactory.Instance, PowerType> powerTypeFactory) {
        JsonObject jo = je.getAsJsonObject();
        ResourceLocation factoryId = ResourceLocation.tryParse(GsonHelper.getAsString(jo, "type"));
        if(isMultiple(factoryId)) {
            factoryId = SIMPLE;
            if(isSubPower) {
                throw new JsonSyntaxException("Power type \"" + MULTIPLE.toString() + "\" may not be used for a sub-power of "
                    + "another \"" + MULTIPLE.toString() + "\" power.");
            }
        }
        Optional<PowerFactory> optionalFactory = ApoliRegistries.POWER_FACTORY.getOptional(factoryId);
        if(!optionalFactory.isPresent()) {
            if(NamespaceAlias.hasAlias(factoryId)) {
                optionalFactory = ApoliRegistries.POWER_FACTORY.getOptional(NamespaceAlias.resolveAlias(factoryId));
            }
            if(!optionalFactory.isPresent()) {
                throw new JsonSyntaxException("Power type \"" + factoryId.toString() + "\" is not defined.");
            }
        }
        PowerFactory.Instance factoryInstance = optionalFactory.get().read(jo);
        PowerType type = powerTypeFactory.apply(id, factoryInstance);
        int priority = GsonHelper.getAsInt(jo, "loading_priority", 0);
        String name = GsonHelper.getAsString(jo, "name", "");
        String description = GsonHelper.getAsString(jo, "description", "");
        boolean hidden = GsonHelper.getAsBoolean(jo, "hidden", false);
        if(hidden || isSubPower) {
            type.setHidden();
        }
        type.setTranslationKeys(name, description);
        if(!PowerTypeRegistry.contains(id)) {
            PowerTypeRegistry.register(id, type);
            loadingPriorities.put(id, priority);
        } else {
            if(loadingPriorities.get(id) < priority) {
                PowerTypeRegistry.update(id, type);
                loadingPriorities.put(id, priority);
            }
        }
        return type;
    }

    private boolean isMultiple(ResourceLocation id) {
        if(MULTIPLE.equals(id)) {
            return true;
        }
        if(NamespaceAlias.hasAlias(id)) {
            return MULTIPLE.equals(NamespaceAlias.resolveAlias(id));
        }
        return false;
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Apoli.MODID, "powers");
    }

    private static <T extends Power> PowerType<T> register(String path, PowerType<T> type) {
        return new PowerTypeReference<>(new ResourceLocation(Apoli.MODID, path));
        //return PowerTypeRegistry.register(new Identifier(Origins.MODID, path), type);
    }
}
