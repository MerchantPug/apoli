package io.github.edwinmindcraft.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record FireProjectileConfiguration(int duration, HudRender hudRender, EntityType<?> entityType,
										  int projectileCount, float speed, float divergence,
										  @Nullable SoundEvent soundEvent, @Nullable CompoundTag tag,
										  IActivePower.Key key) implements IActiveCooldownPowerConfiguration {

	public static final Codec<FireProjectileConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(FireProjectileConfiguration::duration),
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(FireProjectileConfiguration::hudRender),
			Registry.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(FireProjectileConfiguration::entityType),
			CalioCodecHelper.optionalField(Codec.INT, "count", 1).forGetter(FireProjectileConfiguration::projectileCount),
			CalioCodecHelper.optionalField(Codec.FLOAT, "speed", 1.5F).forGetter(FireProjectileConfiguration::speed),
			CalioCodecHelper.optionalField(Codec.FLOAT, "divergence", 1.0F).forGetter(FireProjectileConfiguration::divergence),
			CalioCodecHelper.optionalField(SerializableDataTypes.SOUND_EVENT, "sound").forGetter(x -> Optional.ofNullable(x.soundEvent())),
			CalioCodecHelper.optionalField(SerializableDataTypes.NBT, "tag").forGetter(x -> Optional.ofNullable(x.tag())),
			CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY).forGetter(FireProjectileConfiguration::key)
	).apply(instance, (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> new FireProjectileConfiguration(t1, t2, t3, t4, t5, t6, t7.orElse(null), t8.orElse(null), t9)));


	public void fireProjectiles(LivingEntity player) {
		if (this.soundEvent != null) {
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), this.soundEvent, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
		}
		if (!player.level.isClientSide()) {
			for (int i = 0; i < this.projectileCount; i++) {
				this.fireProjectile(player);
			}
		}
	}

	private void fireProjectile(LivingEntity source) {
		if (this.entityType() != null) {
			Entity entity = this.entityType().create(source.level);
			if (entity == null) {
				return;
			}
			Vec3 rotationVector = source.getLookAngle();
			float yaw = source.getYRot();
			float pitch = source.getXRot();
			Vec3 spawnPos = source.position().add(0, source.getEyeHeightAccess(source.getPose(), source.getDimensions(source.getPose())), 0).add(rotationVector);
			entity.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), pitch, yaw);
			if (entity instanceof Projectile projectile) {
				if (entity instanceof AbstractHurtingProjectile ahp) {
					ahp.xPower = rotationVector.x * this.speed;
					ahp.yPower = rotationVector.y * this.speed;
					ahp.zPower = rotationVector.z * this.speed;
				}
				projectile.setOwner(source);
				projectile.shootFromRotation(source, pitch, yaw, 0F, this.speed, this.divergence);
			} else {
				float f = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
				float g = -Mth.sin(pitch * 0.017453292F);
				float h = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
				Vec3 vec3d = (new Vec3(f, g, h)).normalize().add(source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence, source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence, source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence).scale(this.speed());
				entity.setDeltaMovement(vec3d);
				Vec3 entityVelo = source.getDeltaMovement();
				entity.setDeltaMovement(entity.getDeltaMovement().add(entityVelo.x, source.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
			}
			if (this.tag != null) {
				CompoundTag mergedTag = entity.saveWithoutId(new CompoundTag());
				mergedTag.merge(this.tag);
				entity.load(mergedTag);
			}
			source.level.addFreshEntity(entity);
		}
	}
}
