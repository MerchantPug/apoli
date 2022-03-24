package io.github.edwinmindcraft.apoli.api.configuration;

import net.minecraft.core.Holder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an {@link Holder} as needing to be bound.
 */
@Target(ElementType.RECORD_COMPONENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface MustBeBound {
	//TODO Disable configurations that are missing a binding.
}
