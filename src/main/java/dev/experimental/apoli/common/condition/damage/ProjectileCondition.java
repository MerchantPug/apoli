package dev.experimental.apoli.common.condition.damage;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import java.util.Optional;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ProjectileCondition extends DamageCondition<FieldConfiguration<Optional<EntityType<?>>>> {

	public ProjectileCondition() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.ENTITY_TYPE, "projectile"));
	}

	@Override
	protected boolean check(FieldConfiguration<Optional<EntityType<?>>> configuration, DamageSource source, float amount) {
		if (source instanceof IndirectEntityDamageSource) {
			Entity projectile = source.getDirectEntity();
			return projectile != null && configuration.value().map(projectile.getType()::equals).orElse(true);
		}
		return false;
	}
}
