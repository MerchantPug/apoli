package dev.experimental.apoli.common.power.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.power.IActivePower;
import dev.experimental.apoli.api.power.configuration.power.IActiveCooldownPowerConfiguration;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.mixin.EyeHeightAccess;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record FireProjectileConfiguration(int duration, HudRender hudRender, EntityType<?> entityType,
										  int projectileCount, float speed, float divergence,
										  @Nullable SoundEvent soundEvent, @Nullable NbtCompound tag,
										  IActivePower.Key key) implements IActiveCooldownPowerConfiguration {

	public static final Codec<FireProjectileConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(FireProjectileConfiguration::duration),
			ApoliDataTypes.HUD_RENDER.fieldOf("hud_render").forGetter(FireProjectileConfiguration::hudRender),
			Registry.ENTITY_TYPE.fieldOf("entity_type").forGetter(FireProjectileConfiguration::entityType),
			Codec.INT.optionalFieldOf("count", 1).forGetter(FireProjectileConfiguration::projectileCount),
			Codec.FLOAT.optionalFieldOf("speed", 1.5F).forGetter(FireProjectileConfiguration::speed),
			Codec.FLOAT.optionalFieldOf("divergence", 1.0F).forGetter(FireProjectileConfiguration::divergence),
			SerializableDataTypes.SOUND_EVENT.optionalFieldOf("sound").forGetter(x -> Optional.ofNullable(x.soundEvent())),
			SerializableDataTypes.NBT.optionalFieldOf("tag").forGetter(x -> Optional.ofNullable(x.tag())),
			IActivePower.Key.BACKWARD_COMPATIBLE_CODEC.optionalFieldOf("key", IActivePower.Key.PRIMARY).forGetter(FireProjectileConfiguration::key)
	).apply(instance, (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> new FireProjectileConfiguration(t1, t2, t3, t4, t5, t6, t7.orElse(null), t8.orElse(null), t9)));


	public void fireProjectiles(LivingEntity player) {
		if (this.soundEvent != null) {
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), this.soundEvent, SoundCategory.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
		}
		if (!player.world.isClient) {
			for (int i = 0; i < this.projectileCount; i++) {
				this.fireProjectile(player);
			}
		}
	}

	private void fireProjectile(LivingEntity source) {
		if (this.entityType() != null) {
			Entity entity = this.entityType().create(source.world);
			if (entity == null) {
				return;
			}
			Vec3d rotationVector = source.getRotationVector();
			float yaw = source.getYaw();
			float pitch = source.getPitch();
			Vec3d spawnPos = source.getPos().add(0, ((EyeHeightAccess) source).callGetEyeHeight(source.getPose(), source.getDimensions(source.getPose())), 0).add(rotationVector);
			entity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), pitch, yaw);
			if (entity instanceof ProjectileEntity) {
				if (entity instanceof ExplosiveProjectileEntity) {
					ExplosiveProjectileEntity explosiveProjectileEntity = (ExplosiveProjectileEntity) entity;
					explosiveProjectileEntity.powerX = rotationVector.x * this.speed;
					explosiveProjectileEntity.powerY = rotationVector.y * this.speed;
					explosiveProjectileEntity.powerZ = rotationVector.z * this.speed;
				}
				ProjectileEntity projectile = (ProjectileEntity) entity;
				projectile.setOwner(source);
				projectile.setProperties(source, pitch, yaw, 0F, this.speed, this.divergence);
			} else {
				float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
				float g = -MathHelper.sin(pitch * 0.017453292F);
				float h = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
				Vec3d vec3d = (new Vec3d(f, g, h)).normalize().add(source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence, source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence, source.getRandom().nextGaussian() * 0.007499999832361937D * (double) this.divergence).multiply(this.speed);
				entity.setVelocity(vec3d);
				Vec3d entityVelo = source.getVelocity();
				entity.setVelocity(entity.getVelocity().add(entityVelo.x, source.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
			}
			if (this.tag != null) {
				NbtCompound mergedTag = entity.writeNbt(new NbtCompound());
				mergedTag.copyFrom(this.tag);
				entity.readNbt(mergedTag);
			}
			source.world.spawnEntity(entity);
		}
	}
}
