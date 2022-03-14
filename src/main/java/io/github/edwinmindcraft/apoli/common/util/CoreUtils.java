package io.github.edwinmindcraft.apoli.common.util;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.common.power.RestrictArmorPower;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.coremod.api.ASMAPI;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class CoreUtils {
	/**
	 * Checks armor equipment conditions.
	 *
	 * @param entity The entity to check the conditions for.
	 * @param slot   The slot to check the item against.
	 * @param stack  The stack that is being placed.
	 *
	 * @return {@code true} if there exists a {@link ApoliPowers#RESTRICT_ARMOR} or {@link ApoliPowers#CONDITIONED_RESTRICT_ARMOR} that would prevent armor from being
	 * equipped, or if the item is an {@link Items#ELYTRA} and the player has {@link ApoliPowers#ELYTRA_FLIGHT}
	 */
	public static boolean isItemForbidden(Entity entity, EquipmentSlot slot, ItemStack stack) {
		if (!(entity instanceof LivingEntity living))
			return false;
		return RestrictArmorPower.isForbidden(living, slot, stack) ||
			   (stack.is(Items.ELYTRA) && IPowerContainer.hasPower(living, ApoliPowers.ELYTRA_FLIGHT.get()));
	}

	public static BlockState getInWallBlockState(LivingEntity playerEntity) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int i = 0; i < 8; ++i) {
			double d = playerEntity.getX() + (double) (((float) (i % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			double e = playerEntity.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
			double f = playerEntity.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
			mutable.set(d, e, f);
			BlockState blockState = playerEntity.level.getBlockState(mutable);
			if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(playerEntity.level, mutable)) {
				return blockState;
			}
		}

		return null;
	}

	public static float modifyFriction(float friction, LevelReader level, BlockPos pos, @Nullable Entity entity) {
		if (entity != null) {
			BlockInWorld blockInWorld = new BlockInWorld(level, pos, true);
			return IPowerContainer.modify(entity, ApoliPowers.MODIFY_SLIPPERINESS.get(), friction, p -> ConfiguredBlockCondition.check(p.getConfiguration().condition(), blockInWorld));
		}
		return friction;
	}
}
