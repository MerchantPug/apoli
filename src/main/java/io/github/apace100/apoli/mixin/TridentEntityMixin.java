package io.github.apace100.apoli.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow {

    protected TridentEntityMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "onEntityHit", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;trident(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;"))
    private float modifyProjectileDamageDealt(float original, EntityHitResult entityHitResult) {
        Entity owner = this.getOwner();
        if(owner != null) {
            Entity target = entityHitResult.getEntity();
            DamageSource source = DamageSource.trident(this, owner);
            return PowerHolderComponent.modify(owner, ModifyProjectileDamagePower.class, original, p -> p.doesApply(source, original, target instanceof LivingEntity ? (LivingEntity)target : null), p -> p.executeActions(target));
        }
        return original;
    }
}
