package dev.experimental.apoli.common.power;

import dev.experimental.apoli.api.ApoliAPI;
import dev.experimental.apoli.api.power.configuration.ConfiguredEntityCondition;
import dev.experimental.apoli.api.power.configuration.ConfiguredPower;
import dev.experimental.apoli.api.power.factory.PowerFactory;
import dev.experimental.apoli.common.power.configuration.ClimbingConfiguration;
import dev.experimental.apoli.common.registry.ModPowers;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ClimbingPower extends PowerFactory<ClimbingConfiguration> {
	public static boolean check(LivingEntity player, Consumer<BlockPos> climbingPosSetter) {
		List<ConfiguredPower<ClimbingConfiguration, ClimbingPower>> climbingPowers = ApoliAPI.getPowerContainer(player).getPowers(ModPowers.CLIMBING.get());
		if (climbingPowers.size() > 0) {
			if (climbingPowers.stream().anyMatch(x -> x.isActive(player))) {
				climbingPosSetter.accept(player.blockPosition());
				return true;
			}
			return player.isSuppressingSlidingDownLadder() && climbingPowers.stream().anyMatch(x -> x.getFactory().canHold(x, player));
		}
		return false;
	}

	public ClimbingPower() {
		super(ClimbingConfiguration.CODEC);
	}

	public boolean canHold(ConfiguredPower<ClimbingConfiguration, ?> configuration, LivingEntity player) {
		ConfiguredEntityCondition<?, ?> holdingCondition = configuration.getConfiguration().condition();
		return configuration.getConfiguration().allowHolding() && (holdingCondition == null ? configuration.isActive(player) : holdingCondition.check(player));
	}
}
