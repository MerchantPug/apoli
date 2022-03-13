package io.github.apace100.apoli.power;

import io.github.apace100.apoli.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PowerTypeReference<T extends Power> extends PowerType<T> {

    private PowerType<T> referencedPowerType;

    public PowerTypeReference(ResourceLocation id) {
        super(id, null);
    }

    @Override
    public PowerFactory<T>.Instance getFactory() {
		this.getReferencedPowerType();
        if(this.referencedPowerType == null) {
            return null;
        }
        return this.referencedPowerType.getFactory();
    }

    @Override
    public boolean isActive(Entity entity) {
		this.getReferencedPowerType();
        if(this.referencedPowerType == null) {
            return false;
        }
        return this.referencedPowerType.isActive(entity);
    }

    @Override
    public T get(Entity entity) {
		this.getReferencedPowerType();
        if(this.referencedPowerType == null) {
            return null;
        }
        return this.referencedPowerType.get(entity);
    }

    @SuppressWarnings("unchecked")
    public PowerType<T> getReferencedPowerType() {
        if(this.isReferenceInvalid()) {
            try {
                this.referencedPowerType = null;
                this.referencedPowerType = (PowerType<T>) PowerTypeRegistry.get(this.getIdentifier());
            } catch(IllegalArgumentException e) {
                //cooldown = 600;
                //Apoli.LOGGER.warn("Invalid PowerTypeReference: no power type exists with id \"" + getIdentifier() + "\"");
            }
        }
        return this.referencedPowerType;
    }

    @SuppressWarnings("unchecked")
    private boolean isReferenceInvalid() {
        if(this.referencedPowerType != null) {
            if(PowerTypeRegistry.contains(this.referencedPowerType.getIdentifier())) {
                PowerType<T> type = (PowerType<T>) PowerTypeRegistry.get(this.referencedPowerType.getIdentifier());
                return type != this.referencedPowerType;
            }
        }
        return true;
    }
}
