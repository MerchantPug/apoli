package io.github.apace100.apoli.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Power {

	protected LivingEntity entity;
	protected PowerType<?> type;

	private boolean shouldTick = false;
	private boolean shouldTickWhenInactive = false;

	protected final List<Predicate<Entity>> conditions;

	public Power(PowerType<?> type, LivingEntity entity) {
		this.type = type;
		this.entity = entity;
		this.conditions = new LinkedList<>();
	}

	public Power addCondition(Predicate<Entity> condition) {
		this.conditions.add(condition);
		return this;
	}

	protected void setTicking() {
		this.setTicking(false);
	}

	protected void setTicking(boolean evenWhenInactive) {
		this.shouldTick = true;
		this.shouldTickWhenInactive = evenWhenInactive;
	}

	public boolean shouldTick() {
		return this.shouldTick;
	}

	public boolean shouldTickWhenInactive() {
		return this.shouldTickWhenInactive;
	}

	public void tick() {

	}

	public void onGained() {

	}

	public void onLost() {

	}

	public void onAdded() {

	}

	public void onRemoved() {

	}

	public void onRespawn() {

	}

	public boolean isActive() {
		return this.conditions.stream().allMatch(condition -> condition.test(this.entity));
	}

	public Tag toTag() {
		return new CompoundTag();
	}

	public void fromTag(Tag tag) {

	}

	public PowerType<?> getType() {
		return this.type;
	}

    /*public static PowerFactory createSimpleFactory(BiFunction<PowerType, LivingEntity, Power> powerConstructor, Identifier identifier) {
        return new PowerFactory<>(identifier,
            new SerializableData(), data -> powerConstructor::apply).allowCondition();
    }*/
}
