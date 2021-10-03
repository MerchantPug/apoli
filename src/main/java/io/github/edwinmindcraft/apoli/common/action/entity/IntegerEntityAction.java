package io.github.edwinmindcraft.apoli.common.action.entity;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class IntegerEntityAction extends EntityAction<FieldConfiguration<Integer>> {

	public static IntegerEntityAction ofLiving(BiConsumer<LivingEntity, Integer> action, String field) {
		return new IntegerEntityAction((e, i) -> {if (e instanceof LivingEntity le) action.accept(le, i);}, field);
	}

	public static IntegerEntityAction ofPlayer(BiConsumer<Player, Integer> action, String field) {
		return new IntegerEntityAction((e, i) -> {if (e instanceof Player le) action.accept(le, i);}, field);
	}

	private final BiConsumer<Entity, Integer> action;

	public IntegerEntityAction(BiConsumer<Entity, Integer> action, String field) {
		super(FieldConfiguration.codec(Codec.INT, field));
		this.action = action;
	}

	@Override
	public void execute(FieldConfiguration<Integer> configuration, Entity entity) {
		this.action.accept(entity, configuration.value());
	}
}
