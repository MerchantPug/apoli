package io.github.apace100.apoli.power;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.tuple.Triple;
import virtuoel.pehkui.api.*;

import java.util.*;
import java.util.stream.Collectors;

public class ModifyScalePower extends ValueModifyingPower {

    public boolean hasChangedSize;
    private final List<Identifier> scaleIdentifiers = new ArrayList<>();
    private boolean animation;

    public static final List<Triple<UUID, Identifier, Float>> SCALE_CACHE = new ArrayList<>();

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ModifyScalePower>(Apoli.identifier("modify_scale"),
                new SerializableData()
                        .add("scale", SerializableDataTypes.IDENTIFIER, null)
                        .add("scales", SerializableDataTypes.IDENTIFIERS, null)
                        .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", SerializableDataTypes.ATTRIBUTE_MODIFIERS, null)
                        .add("animation", SerializableDataTypes.BOOLEAN, true),
                data ->
                        (type, entity) -> {
                            ModifyScalePower power = new ModifyScalePower(type, entity, data.getBoolean("animation"));
                            if(data.isPresent("scale")) {
                                power.addScaleIdentifier(data.getId("scale"));
                            }
                            if(data.isPresent("scales")) {
                                ((List<Identifier>) data.get("scales")).forEach(power::addScaleIdentifier);
                            }
                            if(data.isPresent("modifier")) {
                                power.addModifier(data.getModifier("modifier"));
                            }
                            if(data.isPresent("modifiers")) {
                                ((List<EntityAttributeModifier>)data.get("modifiers")).forEach(power::addModifier);
                            }
                            if(!data.isPresent("scale") && !data.isPresent("scales")) {
                                power.addScaleIdentifier(new Identifier("pehkui", "base"));
                            }
                            return power;
                        }).allowCondition();
    }

    public void addScaleIdentifier(Identifier id) {
        this.scaleIdentifiers.add(id);
    }

    public void tick() {
        if (!FabricLoader.getInstance().isModLoaded("pehkui")) return;
        addScaleBeforePower();
        updateScaleCacheIfChanged();
        if (!this.isActive()) {
            this.removeScales();
        } else {
            this.addScales();
        }
    }
    public void onRemoved() {
        if (!FabricLoader.getInstance().isModLoaded("pehkui")) return;
        revokeScales();
    }

    private void addScales() {
        if (hasChangedSize) return;
        this.scaleIdentifiers.forEach(identifier -> {
            Apoli.LOGGER.info("Add scales");
            ScaleType type = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier);
            ScaleData data = type.getScaleData(entity);
            if (SCALE_CACHE.stream().noneMatch(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)) return;
            float scaleBefore = SCALE_CACHE.stream()
                    .filter(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)
                    .toList()
                    .get(0)
                    .getRight();
            float modifiedScale = PowerHolderComponent.modify(entity, ModifyScalePower.class, scaleBefore);
            data.setTargetScale(modifiedScale);
            data.onUpdate();
            hasChangedSize = true;
        });
    }

    private void removeScales() {
        if (!hasChangedSize) return;
        this.scaleIdentifiers.forEach(identifier -> {
            ScaleType type = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier);
            ScaleData data = type.getScaleData(entity);
            if (SCALE_CACHE.stream().noneMatch(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)) return;

            float scaleBefore = SCALE_CACHE.stream()
                    .filter(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)
                    .toList()
                    .get(0)
                    .getRight();

            data.setTargetScale(PowerHolderComponent.modify(entity, ModifyScalePower.class, scaleBefore));
            data.onUpdate();
            hasChangedSize = false;
        });
    }

    private void updateScaleCacheIfChanged() {
        this.scaleIdentifiers.forEach(identifier -> {
            ScaleType type = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier);
            ScaleData data = type.getScaleData(entity);

            if (SCALE_CACHE.stream().noneMatch(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)) return;
            float scaleBefore = SCALE_CACHE.stream()
                    .filter(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)
                    .toList()
                    .get(0)
                    .getRight();

            float modifiedScale = PowerHolderComponent.modify(entity, ModifyScalePower.class, scaleBefore);

            if (PowerHolderComponent.KEY.get(entity).getPowers(ModifyScalePower.class, true).stream().anyMatch(power -> power.hasChangedSize)) return;

            SCALE_CACHE.removeIf(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier);
            SCALE_CACHE.add(Triple.of(entity.getUuid(), identifier, data.getTargetScale()));
            Apoli.LOGGER.info(SCALE_CACHE
                    .stream()
                    .filter(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)
                    .toList()
                    .get(0)
                    .getRight());
            data.setTargetScale(scaleBefore);
            data.onUpdate();
            this.hasChangedSize = false;
        });
    }

    private void addScaleBeforePower() {
        this.scaleIdentifiers.forEach(identifier -> {
            ScaleType type = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier);
            ScaleData data = type.getScaleData(entity);
            if (SCALE_CACHE.stream().noneMatch(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)) {
                SCALE_CACHE.add(Triple.of(entity.getUuid(), identifier, data.getScale()));
            }
        });
    }

    private void revokeScales() {
        int powerAmountIncludingInactive = PowerHolderComponent.KEY.get(entity).getPowers(ModifyScalePower.class, true).size();
        this.scaleIdentifiers.forEach(identifier -> {
            ScaleType type = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier);
            ScaleData data = type.getScaleData(entity);
            List<Triple<UUID, Identifier, Float>> tripleList = SCALE_CACHE;
            if (tripleList.stream().noneMatch(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)) return;
            float scaleBefore = tripleList
                    .stream()
                    .filter(triple -> triple.getLeft() == entity.getUuid() && triple.getMiddle() == identifier)
                    .toList()
                    .get(0)
                    .getRight();
            data.setScale(scaleBefore);
            data.onUpdate();
            if (powerAmountIncludingInactive == 0) {
                SCALE_CACHE.remove(0);
            }
        });
    }

    public ModifyScalePower(PowerType<?> type, LivingEntity entity, boolean animation) {
        super(type, entity);
        this.animation = animation;
        this.setTicking(true);
    }
}