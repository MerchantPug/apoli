package io.github.edwinmindcraft.apoli.common.registry;

import io.github.apace100.apoli.command.EntityConditionArgument;
import io.github.apace100.apoli.command.PowerOperation;
import io.github.apace100.apoli.command.PowerTypeArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.RegistryObject;

public class ApoliArgumentTypes {

	public static final RegistryObject<ArgumentTypeInfo<PowerTypeArgumentType, ?>> POWER_TYPE = ApoliRegisters.ARGUMENT_TYPES.register("power", () -> SingletonArgumentInfo.contextFree(PowerTypeArgumentType::power));
	public static final RegistryObject<ArgumentTypeInfo<PowerOperation, ?>> POWER_OPERATION = ApoliRegisters.ARGUMENT_TYPES.register("power_operation", () -> SingletonArgumentInfo.contextFree(PowerOperation::operation));
	public static final RegistryObject<ArgumentTypeInfo<EntityConditionArgument, ?>> ENTITY_CONDITION = ApoliRegisters.ARGUMENT_TYPES.register("entity_condition", () -> SingletonArgumentInfo.contextFree(EntityConditionArgument::entityCondition));

	public static void bootstrap() {}

	public static void initialize() {
		ArgumentTypeInfos.registerByClass(PowerTypeArgumentType.class, POWER_TYPE.get());
		ArgumentTypeInfos.registerByClass(PowerOperation.class, POWER_OPERATION.get());
		ArgumentTypeInfos.registerByClass(EntityConditionArgument.class, ENTITY_CONDITION.get());
	}
}
