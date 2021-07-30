package io.github.apace100.apoli.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ShaderPower extends Power {

    private final ResourceLocation shaderLocation;

    public ShaderPower(PowerType<?> type, LivingEntity entity, ResourceLocation shaderLocation) {
        super(type, entity);
        this.shaderLocation = shaderLocation;
    }

    public ResourceLocation getShaderLocation() {
        return shaderLocation;
    }
}
