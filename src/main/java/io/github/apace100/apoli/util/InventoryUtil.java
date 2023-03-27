package io.github.apace100.apoli.util;

import io.github.apace100.apoli.mixin.ItemSlotArgumentTypeAccessor;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.InventoryPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.InventoryConfiguration;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;

public class InventoryUtil {

    public enum InventoryType {
        INVENTORY,
        POWER
    }

    public static Set<Integer> getSlots(ListConfiguration<ArgumentWrapper<Integer>> slotArgumentTypes) {
        Set<Integer> slots = new HashSet<>();

        if (slotArgumentTypes.entries().isEmpty()) {
            SlotArgument itemSlotArgumentType = new SlotArgument();
            Map<String, Integer> slotNamesWithId = ((ItemSlotArgumentTypeAccessor) itemSlotArgumentType).getSlotNamesToSlotCommandId();
            slots.addAll(slotNamesWithId.values());
        } else {
            for (ArgumentWrapper<Integer> slotArgumentType : slotArgumentTypes.entries()) {
                slots.add(slotArgumentType.get());
            }
        }

        return slots;
    }

    public static void modifyInventory(ListConfiguration<ArgumentWrapper<Integer>> slotArgumentTypes,
                                       Holder<ConfiguredEntityAction<?, ?>> entityAction,
                                       Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                       Holder<ConfiguredItemAction<?, ?>> itemAction,
                                       Entity entity, Optional<ResourceLocation> powerId) {

        Set<Integer> slots = getSlots(slotArgumentTypes);

        if (powerId.isEmpty()) {
            for (Integer slot : slots) {
                SlotAccess stackReference = entity.getSlot(slot);
                if (stackReference != SlotAccess.NULL) {
                    ItemStack currentItemStack = stackReference.get();
                    if (!currentItemStack.isEmpty()) {
                        if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                            ConfiguredEntityAction.execute(entityAction, entity);
                            Mutable<ItemStack> newStack = new MutableObject<>(currentItemStack);
                            ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                            stackReference.set(newStack.getValue());
                        }
                    }
                }
            }
        }

        else {
            ConfiguredPower<?, ?> power = IPowerContainer.get(entity).resolve()
                    .map(x -> x.getPower(powerId.get())).map(Holder::value).orElse(null);
            if (power == null || !(power.getFactory() instanceof InventoryPower)) return;
            ConfiguredPower<InventoryConfiguration, InventoryPower> inventoryPower = (ConfiguredPower<InventoryConfiguration, InventoryPower>)power;
            slots.removeIf(slot -> slot > inventoryPower.getFactory().getSize());
            for (int i = 0; i < inventoryPower.getFactory().getSize(); i++) {
                if (!slots.isEmpty() && !slots.contains(i)) continue;
                Container container = inventoryPower.getFactory().getInventory(inventoryPower, entity);
                ItemStack currentItemStack = container.getItem(i);
                if (!currentItemStack.isEmpty()) {
                    if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                        ConfiguredEntityAction.execute(entityAction, entity);
                        Mutable<ItemStack> newStack = new MutableObject<>(currentItemStack);
                        ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                        container.setItem(i, newStack.getValue());
                    }
                }
            }
        }

    }

    public static void replaceInventory(ListConfiguration<ArgumentWrapper<Integer>> slotArgumentTypes,
                                        ItemStack replacementStack,
                                        Holder<ConfiguredEntityAction<?, ?>> entityAction,
                                        Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                        Holder<ConfiguredItemAction<?, ?>> itemAction,
                                        Entity entity, Optional<ResourceLocation> powerId) {

        Set<Integer> slots = getSlots(slotArgumentTypes);

        if (powerId.isEmpty()) {
            for (Integer slot : slots) {
                SlotAccess stackReference = entity.getSlot(slot);
                if (stackReference != SlotAccess.NULL) {
                    ItemStack currentItemStack = stackReference.get();
                    if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                        ConfiguredEntityAction.execute(entityAction, entity);
                        Mutable<ItemStack> newStack = new MutableObject<>(replacementStack.copy());
                        ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                        stackReference.set(newStack.getValue());
                    }
                }
            }
        }

        else {
            ConfiguredPower<?, ?> power = IPowerContainer.get(entity).resolve()
                    .map(x -> x.getPower(powerId.get())).map(Holder::value).orElse(null);
            if (power == null || !(power.getFactory() instanceof InventoryPower)) return;
            ConfiguredPower<InventoryConfiguration, InventoryPower> inventoryPower = (ConfiguredPower<InventoryConfiguration, InventoryPower>)power;
            slots.removeIf(slot -> slot > inventoryPower.getFactory().getSize());
            for (int i = 0; i < inventoryPower.getFactory().getSize(); i++) {
                if (!slots.isEmpty() && !slots.contains(i)) continue;
                Container container = inventoryPower.getFactory().getInventory(inventoryPower, entity);
                ItemStack currentItemStack = container.getItem(i);
                if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                    ConfiguredEntityAction.execute(entityAction, entity);
                    Mutable<ItemStack> newStack = new MutableObject<>(replacementStack.copy());
                    ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                    container.setItem(i, newStack.getValue());
                }
            }
        }

    }

    public static void dropInventory(ListConfiguration<ArgumentWrapper<Integer>> slotArgumentTypes,
                                     Holder<ConfiguredEntityAction<?, ?>> entityAction,
                                     Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                     Holder<ConfiguredItemAction<?, ?>> itemAction,
                                     boolean throwRandomly, boolean retainOwnership,
                                     Entity entity, Optional<ResourceLocation> powerId) {

        Set<Integer> slots = getSlots(slotArgumentTypes);

        if (powerId.isEmpty()) {
            for (Integer slot : slots) {
                SlotAccess stackReference = entity.getSlot(slot);
                if (stackReference != SlotAccess.NULL) {
                    ItemStack currentItemStack = stackReference.get();
                    if (!currentItemStack.isEmpty()) {
                        if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                            ConfiguredEntityAction.execute(entityAction, entity);
                            Mutable<ItemStack> newStack = new MutableObject<>(currentItemStack.copy());
                            ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                            throwItem(entity, currentItemStack, throwRandomly, retainOwnership);
                            stackReference.set(ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        else {
            ConfiguredPower<?, ?> power = IPowerContainer.get(entity).resolve()
                    .map(x -> x.getPower(powerId.get())).map(Holder::value).orElse(null);
            if (power == null || !(power.getFactory() instanceof InventoryPower)) return;
            ConfiguredPower<InventoryConfiguration, InventoryPower> inventoryPower = (ConfiguredPower<InventoryConfiguration, InventoryPower>)power;
            slots.removeIf(slot -> slot > inventoryPower.getFactory().getSize());
            for (int i = 0; i < inventoryPower.getFactory().getSize(); i++) {
                if (!slots.isEmpty() && !slots.contains(i)) continue;
                Container container = inventoryPower.getFactory().getInventory(inventoryPower, entity);
                ItemStack currentItemStack = container.getItem(i);
                if (!currentItemStack.isEmpty()) {
                    if (ConfiguredItemCondition.check(itemCondition, entity.level, currentItemStack)) {
                        ConfiguredEntityAction.execute(entityAction, entity);
                        Mutable<ItemStack> newStack = new MutableObject<>(currentItemStack.copy());
                        ConfiguredItemAction.execute(itemAction, entity.level, newStack);
                        throwItem(entity, currentItemStack, throwRandomly, retainOwnership);
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }

    }

    public static void throwItem(Entity thrower, ItemStack itemStack, boolean throwRandomly, boolean retainOwnership) {

        if (itemStack.isEmpty()) return;
        if (thrower instanceof Player playerEntity && playerEntity.level.isClientSide) playerEntity.swing(InteractionHand.MAIN_HAND);

        double yOffset = thrower.getEyeY() - 0.30000001192092896D;
        ItemEntity itemEntity = new ItemEntity(thrower.level, thrower.getX(), yOffset, thrower.getZ(), itemStack);
        itemEntity.setPickUpDelay(40);

        Random random = new Random();

        float f;
        float g;

        if (retainOwnership) itemEntity.setThrower(thrower.getUUID());
        if (throwRandomly) {
            f = random.nextFloat() * 0.5F;
            g = random.nextFloat() * 6.2831855F;
            itemEntity.setDeltaMovement(- Mth.sin(g) * f, 0.20000000298023224D, Mth.cos(g) * f);
        }
        else {
            f = 0.3F;
            g = Mth.sin(thrower.getXRot() * 0.017453292F);
            float h = Mth.cos(thrower.getXRot() * 0.017453292F);
            float i = Mth.sin(thrower.getYRot() * 0.017453292F);
            float j = Mth.cos(thrower.getYRot() * 0.017453292F);
            float k = random.nextFloat() * 6.2831855F;
            float l = 0.02F * random.nextFloat();
            itemEntity.setDeltaMovement(
                (double) (- i * h * f) + Math.cos(k) * (double) l,
                -g * f + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F,
                (double) (j * h * f) + Math.sin(k) * (double) l
            );
        }

        thrower.level.addFreshEntity(itemEntity);

    }

}