package dev.experimental.apoli.common.action.entity;

import dev.experimental.apoli.api.configuration.NoConfiguration;
import dev.experimental.apoli.api.power.factory.EntityAction;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SimpleEntityAction extends EntityAction<NoConfiguration> {

	public static SimpleEntityAction ofLiving(Consumer<LivingEntity> action) {
		return new SimpleEntityAction((Entity e) -> {if (e instanceof LivingEntity le) action.accept(le);});
	}

	public static SimpleEntityAction ofPlayer(Consumer<Player> action) {
		return new SimpleEntityAction((Entity e) -> {if (e instanceof PlayerEntity le) action.accept(le);});
	}

	private final Consumer<Entity> action;

	public SimpleEntityAction(Consumer<Entity> action) {
		super(NoConfiguration.CODEC);
		this.action = action;
	}

	@Override
	public void execute(NoConfiguration configuration, Entity entity) {
		this.action.accept(entity);
	}
}
