package io.github.apace100.apoli.power.factory.condition;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;

public class DamageConditions {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ConditionFactory<>(Apoli.identifier("constant"), new SerializableData()
            .add("value", SerializableDataTypes.BOOLEAN),
            (data, dmg) -> data.getBoolean("value")));
        register(new ConditionFactory<>(Apoli.identifier("and"), new SerializableData()
            .add("conditions", ApoliDataTypes.DAMAGE_CONDITIONS),
            (data, dmg) -> ((List<ConditionFactory<Pair<DamageSource, Float>>.Instance>)data.get("conditions")).stream().allMatch(
                condition -> condition.test(dmg)
            )));
        register(new ConditionFactory<>(Apoli.identifier("or"), new SerializableData()
            .add("conditions", ApoliDataTypes.DAMAGE_CONDITIONS),
            (data, dmg) -> ((List<ConditionFactory<Pair<DamageSource, Float>>.Instance>)data.get("conditions")).stream().anyMatch(
                condition -> condition.test(dmg)
            )));
        register(new ConditionFactory<>(Apoli.identifier("amount"), new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT),
            (data, dmg) -> ((Comparison)data.get("comparison")).compare(dmg.getRight(), data.getFloat("compare_to"))));
        register(new ConditionFactory<>(Apoli.identifier("fire"), new SerializableData(),
            (data, dmg) -> dmg.getLeft().isFire()));
        register(new ConditionFactory<>(Apoli.identifier("name"), new SerializableData()
            .add("name", SerializableDataTypes.STRING),
            (data, dmg) -> dmg.getLeft().getName().equals(data.getString("name"))));
        register(new ConditionFactory<>(Apoli.identifier("projectile"), new SerializableData()
            .add("projectile", SerializableDataTypes.ENTITY_TYPE, null),
            (data, dmg) -> {
                if(dmg.getLeft() instanceof ProjectileDamageSource) {
                    Entity projectile = ((ProjectileDamageSource)dmg.getLeft()).getSource();
                    if(projectile != null && (!data.isPresent("projectile") || projectile.getType() == (EntityType<?>)data.get("projectile"))) {
                        return true;
                    }
                }
                return false;
            }));
        register(new ConditionFactory<>(Apoli.identifier("attacker"), new SerializableData()
            .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null),
            (data, dmg) -> {
                Entity attacker = dmg.getLeft().getAttacker();
                if(attacker instanceof LivingEntity) {
                    if(!data.isPresent("entity_condition") || ((ConditionFactory<LivingEntity>.Instance)data.get("entity_condition")).test((LivingEntity)attacker)) {
                        return true;
                    }
                }
                return false;
            }));
    }

    private static void register(ConditionFactory<Tuple<DamageSource, Float>> conditionFactory) {
        Registry.register(ApoliRegistries.DAMAGE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
