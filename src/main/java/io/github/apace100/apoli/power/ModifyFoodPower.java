package io.github.apace100.apoli.power;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class ModifyFoodPower extends Power {

    private final Predicate<ItemStack> applicableFood;
    private final List<AttributeModifier> foodModifiers;
    private final List<AttributeModifier> saturationModifiers;
    private final Consumer<Entity> entityActionWhenEaten;

    public ModifyFoodPower(PowerType<?> type, LivingEntity entity, Predicate<ItemStack> applicableFood, List<AttributeModifier> foodModifiers, List<AttributeModifier> saturationModifiers, Consumer<Entity> entityActionWhenEaten) {
        super(type, entity);
        this.applicableFood = applicableFood;
        this.foodModifiers = foodModifiers;
        this.saturationModifiers = saturationModifiers;
        this.entityActionWhenEaten = entityActionWhenEaten;
    }

    public boolean doesApply(ItemStack stack) {
        return applicableFood.test(stack);
    }

    public void eat() {
        if(entityActionWhenEaten != null) {
            entityActionWhenEaten.accept(entity);
        }
    }

    public List<AttributeModifier> getFoodModifiers() {
        return foodModifiers;
    }

    public List<AttributeModifier> getSaturationModifiers() {
        return saturationModifiers;
    }
}
