package io.github.edwinmindcraft.apoli.common;

import io.github.apace100.apoli.Apoli;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.component.IPowerDataCache;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.network.C2SPlayerLandedPacket;
import io.github.edwinmindcraft.apoli.common.power.*;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber(modid = Apoli.MODID)
public class ApoliPowerEventHandler {
	@SubscribeEvent
	public static void modifyBreakSpeed(PlayerEvent.BreakSpeed event) {
		Player player = event.getPlayer();
		Level world = player.getCommandSenderWorld();
		float hardness = event.getState().getDestroySpeed(world, event.getPos());
		if (hardness <= 0)
			return;
		float speed = event.getNewSpeed();
		int toolFactor = ForgeHooks.isCorrectToolForDrops(event.getState(), event.getPlayer()) ? 30 : 100;
		float factor = hardness * toolFactor;
		BlockInWorld cbp = new BlockInWorld(world, event.getPos(), true);
		speed = IPowerContainer.modify(player, ApoliPowers.MODIFY_BREAK_SPEED.get(), speed * factor, p -> ConfiguredBlockCondition.check(p.getConfiguration().condition(), cbp)) / factor;
		event.setNewSpeed(speed);
	}


	@SubscribeEvent
	public static void onSleep(PlayerSleepInBedEvent event) {
		//FIXME I'm not sure this will work properly.
		if (PreventSleepPower.tryPreventSleep(event.getPlayer(), event.getPlayer().getCommandSenderWorld(), event.getPos()))
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
	}

	@SubscribeEvent
	public static void onWakeUp(PlayerWakeUpEvent event) {
		event.getPlayer().getSleepingPos().ifPresent(pos -> ActionOnWakeUpPower.execute(event.getEntityLiving(), pos));
	}

	@SubscribeEvent
	public static void finishUsing(LivingEntityUseItemEvent.Finish event) {
		ActionOnItemUsePower.execute(event.getEntityLiving(), event.getItem(), event.getResultStack());
	}

	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer)
			ActionOnBlockBreakPower.execute(event.getPlayer(), new BlockInWorld(event.getWorld(), event.getPos(), true), !event.isCanceled());
	}

	@SubscribeEvent
	public static void onFall(LivingFallEvent event) {
		LivingEntity entityLiving = event.getEntityLiving();
		//TODO Check if this works. It should since MC1.17 seems to fire this on both sides.
		ActionOnLandPower.execute(entityLiving);
		if (IPowerContainer.getPowers(entityLiving, ApoliPowers.MODIFY_FALLING.get()).stream().anyMatch(x -> !x.getConfiguration().takeFallDamage()))
			event.setDamageMultiplier(0.0F); //Disable fall damage without actually removing distance. This is to avoid breaking compatibility.
	}

	@SubscribeEvent
	public static void modifyDamageTaken(LivingDamageEvent event) {
		LivingEntity entityLiving = event.getEntityLiving();
		event.setAmount(ModifyDamageTakenPower.modify(entityLiving, event.getSource(), event.getAmount()));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void makeInvulnerable(LivingAttackEvent event) {
		LivingEntity entityLiving = event.getEntityLiving();
		if (InvulnerablePower.isInvulnerableTo(entityLiving, event.getSource()))
			event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public static void livingDamage(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		IPowerDataCache.get(target).ifPresent(x -> x.setDamage(event.getAmount()));
		if (source.isProjectile())
			event.setAmount(ModifyDamageDealtPower.modifyProjectile(source.getEntity(), target, source, event.getAmount()));
		else
			event.setAmount(ModifyDamageDealtPower.modifyMelee(source.getEntity(), target, source, event.getAmount()));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void livingDeath(LivingDeathEvent event) {
		//I don't know at what priority I should fire this, but as a precaution, I'm firing it at the highest priority,
		//as actions that happen when you die shouldn't actually happen if you survive.
		IPowerDataCache.get(event.getEntityLiving()).ifPresent(x -> {
			if (PreventDeathPower.tryPreventDeath(event.getEntityLiving(), event.getSource(), x.getDamage()))
				event.setCanceled(true);
		});
	}

	/**
	 * This needs to be executed after COMBAT's jump overhaul.
	 */
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void livingJump(LivingEvent.LivingJumpEvent event) {
		double modified = ModifyJumpPower.apply(event.getEntityLiving(), event.getEntityLiving().getDeltaMovement().y);
		updateJumpHeight(modified, event.getEntityLiving());
	}

	private static void updateJumpHeight(double height, LivingEntity entity) {
		Vec3 vel = entity.getDeltaMovement();
		double delta = height - vel.y;
		if (delta == 0)
			return;
		entity.setDeltaMovement(vel.add(0, delta, 0));
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		if (event.getAmount() > 0 && !event.isCanceled()) {
			SelfActionWhenHitPower.execute(target, source, event.getAmount());
			AttackerActionWhenHitPower.execute(target, source, event.getAmount());
			if (source.getEntity() instanceof LivingEntity living) {
				SelfCombatActionPower.onHit(living, target, source, event.getAmount());
				TargetCombatActionPower.onHit(living, target, source, event.getAmount());
			}
		}
		IPowerDataCache.get(target).ifPresent(x -> x.setDamage(event.getAmount()));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void preventLivingDeath(LivingDeathEvent event) {
		IPowerDataCache.get(event.getEntityLiving()).map(IPowerDataCache::getDamage).ifPresent(x -> {
			if (PreventDeathPower.tryPreventDeath(event.getEntityLiving(), event.getSource(), x)) {
				event.getEntityLiving().setHealth(1.0F);
				event.setCanceled(true);
			}
		});
	}

	@SubscribeEvent
	public static void onLivingKilled(LivingDeathEvent event) {
		IPowerDataCache.get(event.getEntityLiving()).map(IPowerDataCache::getDamage).ifPresent(x -> {
			if (event.getSource().getEntity() instanceof LivingEntity living)
				SelfCombatActionPower.onKill(living, event.getEntityLiving(), event.getSource(), x);
		});
	}

	@SubscribeEvent
	public static void preventBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
		if (PreventBlockActionPower.isUsagePrevented(event.getPlayer(), event.getPos()))
			event.setUseBlock(Event.Result.DENY);
		if (PreventItemActionPower.isUsagePrevented(event.getPlayer(), event.getItemStack()))
			event.setUseItem(Event.Result.DENY);
		if (event.getItemStack().getItem() instanceof ArmorItem) {
			EquipmentSlot slot = Mob.getEquipmentSlotForItem(event.getItemStack());
			if (RestrictArmorPower.isForbidden(event.getPlayer(), slot, event.getItemStack()))
				event.setUseItem(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public static void preventItemUsage(PlayerInteractEvent.RightClickItem event) {
		if (PreventItemActionPower.isUsagePrevented(event.getPlayer(), event.getItemStack()))
			event.setCanceled(true);
		if (event.getItemStack().getItem() instanceof ArmorItem) {
			EquipmentSlot slot = Mob.getEquipmentSlotForItem(event.getItemStack());
			if (RestrictArmorPower.isForbidden(event.getPlayer(), slot, event.getItemStack()))
				event.setCanceled(true);
		}
	}
}
