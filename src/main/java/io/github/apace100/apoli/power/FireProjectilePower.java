package io.github.apace100.apoli.power;

import io.github.apace100.apoli.mixin.EyeHeightAccess;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class FireProjectilePower extends ActiveCooldownPower {

    private final EntityType entityType;
    private final int projectileCount;
    private final float speed;
    private final float divergence;
    private final SoundEvent soundEvent;
    private final CompoundTag tag;

    public FireProjectilePower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, EntityType entityType, int projectileCount, float speed, float divergence, SoundEvent soundEvent, CompoundTag tag) {
        super(type, entity, cooldownDuration, hudRender, null);
        this.entityType = entityType;
        this.projectileCount = projectileCount;
        this.speed = speed;
        this.divergence = divergence;
        this.soundEvent = soundEvent;
        this.tag = tag;
    }

    @Override
    public void onUse() {
        if(canUse()) {
            fireProjectiles();
            use();
        }
    }

    private void fireProjectiles() {
        if(soundEvent != null) {
            entity.level.playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, SoundSource.NEUTRAL, 0.5F, 0.4F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        }
        if (!entity.level.isClientSide) {
            for(int i = 0; i < projectileCount; i++) {
                fireProjectile();
            }
        }
    }

    private void fireProjectile() {
        if(entityType != null) {
            Entity entity = entityType.create(this.entity.level);
            if(entity == null) {
                return;
            }
            Vec3 rotationVector = this.entity.getLookAngle();
            float yaw = this.entity.getYRot();
            float pitch = this.entity.getXRot();
            Vec3 spawnPos = this.entity.position().add(0, ((EyeHeightAccess) this.entity).callGetEyeHeight(this.entity.getPose(), this.entity.getDimensions(this.entity.getPose())), 0).add(rotationVector);
            entity.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), pitch, yaw);
            if(entity instanceof Projectile) {
                if(entity instanceof AbstractHurtingProjectile) {
                    AbstractHurtingProjectile explosiveProjectileEntity = (AbstractHurtingProjectile)entity;
                    explosiveProjectileEntity.xPower = rotationVector.x * speed;
                    explosiveProjectileEntity.yPower = rotationVector.y * speed;
                    explosiveProjectileEntity.zPower = rotationVector.z * speed;
                }
                Projectile projectile = (Projectile)entity;
                projectile.setOwner(this.entity);
                projectile.shootFromRotation(this.entity, pitch, yaw, 0F, speed, divergence);
            } else {
                float f = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
                float g = -Mth.sin(pitch * 0.017453292F);
                float h = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
                Vec3 vec3d = (new Vec3(f, g, h)).normalize().add(this.entity.getRandom().nextGaussian() * 0.007499999832361937D * (double)divergence, this.entity.getRandom().nextGaussian() * 0.007499999832361937D * (double)divergence, this.entity.getRandom().nextGaussian() * 0.007499999832361937D * (double)divergence).scale((double)speed);
                entity.setDeltaMovement(vec3d);
                Vec3 entityVelo = this.entity.getDeltaMovement();
                entity.setDeltaMovement(entity.getDeltaMovement().add(entityVelo.x, this.entity.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
            }
            if(tag != null) {
                CompoundTag mergedTag = entity.saveWithoutId(new CompoundTag());
                mergedTag.merge(tag);
                entity.load(mergedTag);
            }
            this.entity.level.addFreshEntity(entity);
        }
    }
}
