package dev.experiment.helper;

import io.github.apace100.apoli.Apoli;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public record RegistryDefinition<T>(DeferredRegister<T> register, Supplier<IForgeRegistry<T>> registry) {
	public static <T> RegistryDefinition<T> define(String registryName) {
		DeferredRegister<T> deferredRegister = DeferredRegister.create(Apoli.identifier(registryName), Apoli.MODID);
		Supplier<IForgeRegistry<T>> registry = deferredRegister.makeRegistry(() -> new RegistryBuilder<T>().disableSaving());
		return new RegistryDefinition<>(deferredRegister, registry);
	}
}
