package io.github.apace100.apoli.component;

import com.google.common.collect.Lists;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.util.AttributeUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public interface PowerHolderComponent extends AutoSyncedComponent, ServerTickingComponent {

    ComponentKey<PowerHolderComponent> KEY = ComponentRegistry.getOrCreate(Apoli.identifier("powers"), PowerHolderComponent.class);

    void removePower(PowerType<?> powerType, ResourceLocation source);

    int removeAllPowersFromSource(ResourceLocation source);

    List<PowerType<?>> getPowersFromSource(ResourceLocation source);

    boolean addPower(PowerType<?> powerType, ResourceLocation source);

    boolean hasPower(PowerType<?> powerType);

    boolean hasPower(PowerType<?> powerType, ResourceLocation source);

    <T extends Power> T getPower(PowerType<T> powerType);

    List<Power> getPowers();

    Set<PowerType<?>> getPowerTypes(boolean getSubPowerTypes);

    <T extends Power> List<T> getPowers(Class<T> powerClass);

    <T extends Power> List<T> getPowers(Class<T> powerClass, boolean includeInactive);

    List<ResourceLocation> getSources(PowerType<?> powerType);

    void sync();

    static void sync(Entity entity) {
        KEY.sync(entity);
    }

    static <T extends Power> void withPower(Entity entity, Class<T> powerClass, Predicate<T> power, Consumer<T> with) {
        if(entity instanceof LivingEntity) {
            Optional<T> optional = KEY.get(entity).getPowers(powerClass).stream().filter(p -> power == null || power.test(p)).findAny();
            optional.ifPresent(with);
        }
    }

    static <T extends Power> List<T> getPowers(Entity entity, Class<T> powerClass) {
        if(entity instanceof LivingEntity) {
            return KEY.get(entity).getPowers(powerClass);
        }
        return Lists.newArrayList();
    }

    static <T extends Power> boolean hasPower(Entity entity, Class<T> powerClass) {
        if(entity instanceof LivingEntity) {
            return KEY.get(entity).getPowers().stream().anyMatch(p -> powerClass.isAssignableFrom(p.getClass()) && p.isActive());
        }
        return false;
    }

    static <T extends ValueModifyingPower> float modify(Entity entity, Class<T> powerClass, float baseValue) {
        return (float)modify(entity, powerClass, (double)baseValue, null, null);
    }

    static <T extends ValueModifyingPower> float modify(Entity entity, Class<T> powerClass, float baseValue, Predicate<T> powerFilter) {
        return (float)modify(entity, powerClass, (double)baseValue, powerFilter, null);
    }

    static <T extends ValueModifyingPower> float modify(Entity entity, Class<T> powerClass, float baseValue, Predicate<T> powerFilter, Consumer<T> powerAction) {
        return (float)modify(entity, powerClass, (double)baseValue, powerFilter, powerAction);
    }

    static <T extends ValueModifyingPower> double modify(Entity entity, Class<T> powerClass, double baseValue) {
        return modify(entity, powerClass, baseValue, null, null);
    }

    static <T extends ValueModifyingPower> double modify(Entity entity, Class<T> powerClass, double baseValue, Predicate<T> powerFilter, Consumer<T> powerAction) {
        if(entity instanceof Player) {
            List<T> powers = PowerHolderComponent.KEY.get(entity).getPowers(powerClass);
            List<AttributeModifier> mps = powers.stream()
                .filter(p -> powerFilter == null || powerFilter.test(p))
                .flatMap(p -> p.getModifiers().stream()).collect(Collectors.toList());
            if(powerAction != null) {
                powers.stream().filter(p -> powerFilter == null || powerFilter.test(p)).forEach(powerAction);
            }
            return AttributeUtil.sortAndApplyModifiers(mps, baseValue);
        }
        return baseValue;
    }
}
