package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record DamageOverTimeConfiguration(int interval, int delay, float damageEasy,
										  float damage, DamageSource damageSource,
										  @Nullable Enchantment protectionEnchantment,
										  float protectionEffectiveness) implements IDynamicFeatureConfiguration {
	public static final DamageSource GENERIC_DAMAGE = new DamageSource("genericDamageOverTime").bypassArmor().bypassMagic();

	public static final Codec<DamageOverTimeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CalioCodecHelper.optionalField(CalioCodecHelper.INT, "interval", 20).forGetter(DamageOverTimeConfiguration::interval),
			CalioCodecHelper.optionalField(CalioCodecHelper.INT, "onset_delay").forGetter(x -> x.delay() == x.interval() ? Optional.empty() : Optional.of(x.delay())),
			CalioCodecHelper.optionalField(CalioCodecHelper.FLOAT, "damage_easy").forGetter(x -> x.damageEasy() == x.damage() ? Optional.empty() : Optional.of(x.damageEasy())),
			CalioCodecHelper.FLOAT.fieldOf("damage").forGetter(DamageOverTimeConfiguration::damage),
			CalioCodecHelper.optionalField(SerializableDataTypes.DAMAGE_SOURCE, "damage_source", GENERIC_DAMAGE).forGetter(DamageOverTimeConfiguration::damageSource),
			CalioCodecHelper.optionalField(SerializableDataTypes.ENCHANTMENT, "protection_enchantment").forGetter(x -> Optional.ofNullable(x.protectionEnchantment())),
			CalioCodecHelper.optionalField(CalioCodecHelper.FLOAT, "protection_effectiveness", 1.0F).forGetter(DamageOverTimeConfiguration::protectionEffectiveness)
	).apply(instance, (t1, t2, t3, t4, t5, t6, t7) -> new DamageOverTimeConfiguration(t1, t2.orElse(t1), t3.orElse(t4), t4, t5, t6.orElse(null), t7)));
}
