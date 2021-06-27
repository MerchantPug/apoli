package io.github.apace100.apoli.power.factory;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.Tag;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class PowerFactories {

    public static void register() {
        //TODO Introduce PlayerAbilityPower
        register(new PowerFactory<>(Apoli.identifier("creative_flight"),
            new SerializableData(),
            data ->
                (type, player) -> new PlayerAbilityPower(type, player, VanillaAbilities.ALLOW_FLYING))
            .allowCondition());
    }

    private static void register(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
