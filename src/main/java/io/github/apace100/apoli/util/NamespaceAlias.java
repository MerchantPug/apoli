package io.github.apace100.apoli.util;

import io.github.apace100.apoli.Apoli;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;

public final class NamespaceAlias {

    private static final HashMap<String, String> aliasedNamespaces = new HashMap<>();

    public static void addAlias(String fromNamespace, String toNamespace) {
        aliasedNamespaces.put(fromNamespace, toNamespace);
    }

    public static boolean hasAlias(String namespace) {
        return aliasedNamespaces.containsKey(namespace);
    }

    public static boolean hasAlias(ResourceLocation identifier) {
        return hasAlias(identifier.getNamespace());
    }

    public static ResourceLocation resolveAlias(ResourceLocation original) {
        if(!aliasedNamespaces.containsKey(original.getNamespace())) {
            throw new RuntimeException("Tried to resolve a namespace alias for a namespace which didn't have an alias.");
        }
        return new ResourceLocation(aliasedNamespaces.get(original.getNamespace()), original.getPath());
    }
}
