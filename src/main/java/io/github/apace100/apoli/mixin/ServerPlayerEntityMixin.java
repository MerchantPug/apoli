package io.github.apace100.apoli.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import io.github.apace100.apoli.access.EndRespawningEntity;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.ModifyPlayerSpawnPower;
import io.github.apace100.apoli.power.PreventSleepPower;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements ContainerListener, EndRespawningEntity {

    @Shadow
    private ResourceKey<Level> spawnPointDimension;

    @Shadow
    private BlockPos spawnPointPosition;

    @Shadow
    private boolean spawnPointSet;

    @Shadow
    @Final
    public MinecraftServer server;

    @Shadow
    public ServerGamePacketListenerImpl networkHandler;

    @Shadow
    public abstract void displayClientMessage(Component message, boolean actionBar);

    @Shadow
    public boolean notInAnyWorld;

    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float modifyDamageAmount(DamageSource source, float originalAmount) {
        return PowerHolderComponent.modify(this, ModifyDamageTakenPower.class, originalAmount, p -> p.doesApply(source, originalAmount), p -> p.executeActions(source.getEntity()));
    }

    // FRESH_AIR
    @Inject(method = "trySleep", at = @At(value = "INVOKE",target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"), cancellable = true)
    public void preventAvianSleep(BlockPos pos, CallbackInfoReturnable<Either<BedSleepingProblem, Unit>> info) {
        PowerHolderComponent.getPowers(this, PreventSleepPower.class).forEach(p -> {
                if(p.doesPrevent(level, pos)) {
                    if(p.doesAllowSpawnPoint()) {
                        ((ServerPlayer)(Object)this).setRespawnPosition(this.level.dimension(), pos, this.getYRot(), false, true);
                    }
                    info.setReturnValue(Either.left(null));
                    this.displayClientMessage(new TranslatableComponent(p.getMessage()), true);
                }
            }
        );
    }

    @Inject(at = @At("HEAD"), method = "getSpawnPointDimension", cancellable = true)
    private void modifySpawnPointDimension(CallbackInfoReturnable<ResourceKey<Level>> info) {
        if (!this.origins_isEndRespawning && (spawnPointPosition == null || hasObstructedSpawn()) && PowerHolderComponent.getPowers(this, ModifyPlayerSpawnPower.class).size() > 0) {
            ModifyPlayerSpawnPower power = PowerHolderComponent.getPowers(this, ModifyPlayerSpawnPower.class).get(0);
            info.setReturnValue(power.dimension);
        }
    }

    @Inject(at = @At("HEAD"), method = "getSpawnPointPosition", cancellable = true)
    private void modifyPlayerSpawnPosition(CallbackInfoReturnable<BlockPos> info) {
        if(!this.origins_isEndRespawning && PowerHolderComponent.getPowers(this, ModifyPlayerSpawnPower.class).size() > 0) {
            if(spawnPointPosition == null) {
                info.setReturnValue(findPlayerSpawn());
            } else if(hasObstructedSpawn()) {
                networkHandler.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
                info.setReturnValue(findPlayerSpawn());
            }
        }
    }


    @Inject(at = @At("HEAD"), method = "isSpawnPointSet", cancellable = true)
    private void modifySpawnPointSet(CallbackInfoReturnable<Boolean> info) {
        if(!this.origins_isEndRespawning && (spawnPointPosition == null || hasObstructedSpawn()) && PowerHolderComponent.hasPower(this, ModifyPlayerSpawnPower.class)) {
            info.setReturnValue(true);
        }
    }

    private boolean hasObstructedSpawn() {
        ServerLevel world = server.getLevel(spawnPointDimension);
        if(spawnPointPosition != null && world != null) {
            Optional optional = Player.findRespawnPositionAndUseSpawnBlock(world, spawnPointPosition, 0F, spawnPointSet, true);
            return !optional.isPresent();
        }
        return false;
    }

    private BlockPos findPlayerSpawn() {
        ModifyPlayerSpawnPower power = PowerHolderComponent.getPowers(this, ModifyPlayerSpawnPower.class).get(0);
        Tuple<ServerLevel, BlockPos> spawn = power.getSpawn(true);
        if(spawn != null) {
            return spawn.getB();
        }
        return null;
    }

    @Unique
    private boolean origins_isEndRespawning;

    @Override
    public void setEndRespawning(boolean endSpawn) {
        this.origins_isEndRespawning = endSpawn;
    }

    @Override
    public boolean isEndRespawning() {
        return this.origins_isEndRespawning;
    }

    @Override
    public boolean hasRealRespawnPoint() {
        return spawnPointPosition != null && !hasObstructedSpawn();
    }
}
