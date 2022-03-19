package io.github.edwinmindcraft.apoli.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ApoliMixinPlugin implements IMixinConfigPlugin {
	private boolean citadelLoaded;

	private static boolean classExists(String cls) {
		try {
			Class.forName(cls, false, ApoliMixinPlugin.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private boolean earsLoaded;

	@Override
	public void onLoad(String mixinPackage) {
		this.citadelLoaded = classExists("com.github.alexthe666.citadel.client.event.EventGetOutlineColor");
		this.earsLoaded = classExists("com.github.alexthe666.citadel.client.event.EventGetOutlineColor");
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.equals("io.github.apace100.apoli.mixin.forge.ChangeGlowColorMixin"))
			return !this.citadelLoaded; //If citadel is loaded, handle this by event.
		if (mixinClassName.equals("io.github.apace100.apoli.mixin.forge.EarsCompatMixin"))
			return this.earsLoaded;
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
