package io.github.edwinmindcraft.apoli.common.registry;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.component.IPowerDataCache;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.jetbrains.annotations.NotNull;

public class ApoliCapabilities {
	@NotNull
	@CapabilityInject(IPowerContainer.class)
	public static final Capability<IPowerContainer> POWER_CONTAINER = injectFinal();

	@NotNull
	@CapabilityInject(IPowerDataCache.class)
	public static final Capability<IPowerDataCache> POWER_DATA_CACHE = injectFinal();

	/**
	 * Used to trick IntelliJ into giving hiding NPEs for those caps.
	 */
	@NotNull
	private static <T> T injectFinal() {
		return null;
	}
}
