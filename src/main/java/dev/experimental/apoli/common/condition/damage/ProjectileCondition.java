package dev.experimental.apoli.common.condition.damage;

import dev.experimental.apoli.api.configuration.FieldConfiguration;
import dev.experimental.apoli.api.power.factory.DamageCondition;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;

import java.util.Optional;

public class ProjectileCondition extends DamageCondition<FieldConfiguration<Optional<EntityType<?>>>> {

	public ProjectileCondition() {
		super(FieldConfiguration.optionalCodec(SerializableDataTypes.ENTITY_TYPE, "projectile"));
	}

	@Override
	protected boolean check(FieldConfiguration<Optional<EntityType<?>>> configuration, DamageSource source, float amount) {
		if (source instanceof ProjectileDamageSource) {
			Entity projectile = source.getSource();
			return projectile != null && configuration.value().map(projectile.getType()::equals).orElse(true);
		}
		return false;
	}
}
