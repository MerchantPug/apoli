package io.github.edwinmindcraft.apoli.common.action.bientity;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityAction;
import io.github.edwinmindcraft.apoli.common.action.meta.IDelegatedActionConfiguration;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.tuple.Pair;

public class DelegatedBiEntityAction<T extends IDelegatedActionConfiguration<Pair<Entity, Entity>>> extends BiEntityAction<T> {
	public DelegatedBiEntityAction(Codec<T> codec) {
		super(codec);
	}

	@Override
	public void execute(T configuration, Entity user, Entity target) {
		configuration.execute(Pair.of(user, target));
	}
}
