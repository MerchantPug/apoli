package io.github.apace100.apoli.power.factory.action.block;

import io.github.apace100.apoli.action.configuration.ExplodeConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

public class ExplodeAction extends BlockAction<ExplodeConfiguration> {


	public ExplodeAction() {
		super(ExplodeConfiguration.CODEC);
	}

	@Override
	public void execute(ExplodeConfiguration configuration, Level world, BlockPos pos, Direction direction) {
		if (world.isClientSide())
			return;
		ExplosionDamageCalculator calculator = configuration.calculator();
		world.explode(null, DamageSource.explosion((LivingEntity) null), calculator, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, configuration.power(), configuration.createFire(), configuration.destructionType());
	}

	/*public static ActionFactory<Triple<World, BlockPos, Direction>> getFactory() {
		return new ActionFactory<>(Apoli.identifier("explode"),
				new SerializableData()
						.add("power", SerializableDataTypes.FLOAT)
						.add("destruction_type", SerializableDataType.enumValue(Explosion.DestructionType.class), Explosion.DestructionType.BREAK)
						.add("damage_self", SerializableDataTypes.BOOLEAN, true)
						.add("indestructible", ApoliDataTypes.BLOCK_CONDITION, null)
						.add("destructible", ApoliDataTypes.BLOCK_CONDITION, null)
						.add("create_fire", SerializableDataTypes.BOOLEAN, false),
				ExplodeAction::action
		);
	}*/
}
