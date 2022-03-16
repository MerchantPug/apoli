package io.github.apace100.apoli.power.factory.action.block;

import io.github.apace100.apoli.action.configuration.ModifyBlockStateConfiguration;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class ModifyBlockStateAction extends BlockAction<ModifyBlockStateConfiguration> {


	public ModifyBlockStateAction() {
		super(ModifyBlockStateConfiguration.CODEC);
	}

	@Override
	@SuppressWarnings("unckecked")
	public void execute(@NotNull ModifyBlockStateConfiguration configuration, @NotNull Level world, @NotNull BlockPos pos, @NotNull Direction direction) {
		BlockState state = world.getBlockState(pos);
		Collection<Property<?>> properties = state.getProperties();
		String desiredPropertyName = configuration.property();
		Property<?> property = null;
		for (Property<?> p : properties) {
			if (p.getName().equals(desiredPropertyName)) {
				property = p;
				break;
			}
		}
		if (property != null) {
			if (configuration.cycle()) {
				world.setBlockAndUpdate(pos, state.cycle(property));
			} else {
				Object value = state.getValue(property);
				if (configuration.enumValue() != null && value instanceof Enum) {
					modifyEnumState(world, pos, state, property, configuration.enumValue());
				} else if (configuration.value() != null && value instanceof Boolean) {
					world.setBlockAndUpdate(pos, state.setValue((Property<Boolean>) property, configuration.value()));
				} else if (configuration.change() != null && value instanceof Integer) {
					ResourceOperation op = configuration.operation();
					int opValue = configuration.change();
					int newValue = (int) value;
					switch (op) {
						case ADD -> newValue += opValue;
						case SET -> newValue = opValue;
					}
					Property<Integer> integerProperty = (Property<Integer>) property;
					if (integerProperty.getPossibleValues().contains(newValue)) {
						world.setBlockAndUpdate(pos, state.setValue(integerProperty, newValue));
					}
				}
			}
		}
	}

	private static <T extends Comparable<T>> void modifyEnumState(Level world, BlockPos pos, BlockState originalState, Property<T> property, String value) {
		Optional<T> enumValue = property.getValue(value);
		enumValue.ifPresent(v -> world.setBlockAndUpdate(pos, originalState.setValue(property, v)));
	}
}
