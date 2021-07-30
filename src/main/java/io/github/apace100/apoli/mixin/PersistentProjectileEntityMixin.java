package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class PersistentProjectileEntityMixin {

    @ModifyVariable(method = "onEntityHit", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"))
    private int modifyProjectileDamageDealt(int original, EntityHitResult entityHitResult) {
        Entity owner = ((Projectile)(Object)this).getOwner();
        if(owner != null) {
            Entity target = entityHitResult.getEntity();
            DamageSource source = DamageSource.arrow((AbstractArrow)(Object)this, owner);
            return (int) PowerHolderComponent.modify(owner, ModifyProjectileDamagePower.class, original, p -> p.doesApply(source, original, target instanceof LivingEntity ? (LivingEntity)target : null), p -> p.executeActions(target));
        }
        return original;
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean preventDamageWhenZero(Entity entity, DamageSource source, float amount) {
        if(entity instanceof ServerPlayer || amount > 0f) {
            return entity.hurt(source, amount);
        }
        return false;
    }
}
