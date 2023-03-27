package io.github.edwinmindcraft.apoli.common.registry;

import io.github.edwinmindcraft.apoli.common.modifier.*;
import net.minecraftforge.registries.RegistryObject;

public class ApoliModifierOperations {

    public static final RegistryObject<AddBaseEarlyModifierOperation> ADD_BASE_EARLY = ApoliRegisters.MODIFIER_OPERATIONS.register("add_base_early", AddBaseEarlyModifierOperation::new);
    public static final RegistryObject<AddBaseLateModifierOperation> ADD_BASE_LATE = ApoliRegisters.MODIFIER_OPERATIONS.register("add_base_late", AddBaseLateModifierOperation::new);
    public static final RegistryObject<AddTotalLateModifierOperation> ADD_TOTAL_LATE = ApoliRegisters.MODIFIER_OPERATIONS.register("add_total_late", AddTotalLateModifierOperation::new);
    public static final RegistryObject<MaxBaseModifierOperation> MAX_BASE = ApoliRegisters.MODIFIER_OPERATIONS.register("max_base", MaxBaseModifierOperation::new);
    public static final RegistryObject<MaxTotalModifierOperation> MAX_TOTAL = ApoliRegisters.MODIFIER_OPERATIONS.register("max_total", MaxTotalModifierOperation::new);
    public static final RegistryObject<MinBaseModifierOperation> MIN_BASE = ApoliRegisters.MODIFIER_OPERATIONS.register("min_base", MinBaseModifierOperation::new);
    public static final RegistryObject<MinTotalModifierOperation> MIN_TOTAL = ApoliRegisters.MODIFIER_OPERATIONS.register("min_total", MinTotalModifierOperation::new);
    public static final RegistryObject<MultiplyBaseAdditiveModifierOperation> MULTIPLY_BASE_ADDITIVE = ApoliRegisters.MODIFIER_OPERATIONS.register("multiply_base_additive", MultiplyBaseAdditiveModifierOperation::new);
    public static final RegistryObject<MultiplyBaseMultiplicativeModifierOperation> MULTIPLY_BASE_MULTIPLICATIVE = ApoliRegisters.MODIFIER_OPERATIONS.register("multiply_base_multiplicative", MultiplyBaseMultiplicativeModifierOperation::new);
    public static final RegistryObject<MultiplyTotalAdditiveModifierOperation> MULTIPLY_TOTAL_ADDITIVE = ApoliRegisters.MODIFIER_OPERATIONS.register("multiply_total_additive", MultiplyTotalAdditiveModifierOperation::new);
    public static final RegistryObject<MultiplyTotalMultiplicativeModifierOperation> MULTIPLY_TOTAL_MULTIPLICATIVE = ApoliRegisters.MODIFIER_OPERATIONS.register("multiply_total_multiplicative", MultiplyTotalMultiplicativeModifierOperation::new);
    public static final RegistryObject<SetBaseModifierOperation> SET_BASE = ApoliRegisters.MODIFIER_OPERATIONS.register("set_base", SetBaseModifierOperation::new);
    public static final RegistryObject<SetTotalModifierOperation> SET_TOTAL = ApoliRegisters.MODIFIER_OPERATIONS.register("set_total", SetTotalModifierOperation::new);

    public static void bootstrap() {}
}
