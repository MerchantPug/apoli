package io.github.edwinmindcraft.apoli.common.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.checkerframework.checker.units.qual.A;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class PowerUtils {
	private static final Random RANDOM = new Random();
	public static AttributeModifier staticModifier(String name, double value, AttributeModifier.Operation operation, Object... fixed) {
		UUID uuid;
		synchronized (RANDOM) {
			RANDOM.setSeed((long) Objects.hash(fixed) | (long) Objects.hash(name, value, operation) << 32L);
			uuid = new UUID(RANDOM.nextLong(), RANDOM.nextLong());
		}
		return new AttributeModifier(uuid, name, value, operation);
	}
}
