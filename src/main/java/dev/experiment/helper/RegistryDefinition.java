package dev.experiment.helper;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.calio.ClassUtil;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public record RegistryDefinition<T extends IForgeRegistryEntry<T>>(DeferredRegister<T> register,
																   Supplier<IForgeRegistry<T>> registry) {
	public static <T extends IForgeRegistryEntry<T>> RegistryDefinition<T> define(String registryName, T... arr) {
		Class<T> cls = ClassUtil.get(arr);
		DeferredRegister<T> deferredRegister = DeferredRegister.create(cls, Apoli.MODID);
		Supplier<IForgeRegistry<T>> registry = deferredRegister.makeRegistry(registryName, () -> new RegistryBuilder<T>().disableSaving());
		return new RegistryDefinition<>(deferredRegister, registry);
	}
}
