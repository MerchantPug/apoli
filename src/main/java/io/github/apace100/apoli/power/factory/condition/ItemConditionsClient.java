package io.github.apace100.apoli.power.factory.condition;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemConditionsClient {

	@SuppressWarnings("unchecked")
	public static void register() {
        /*register(new ConditionFactory<>(Apoli.identifier("smeltable"), new SerializableData(),
            (data, stack) -> {
                World world = MinecraftClient.getInstance().world;
                if(world == null) {
                    return false;
                }
                Optional<SmeltingRecipe> optional = world.getRecipeManager()
                    .getFirstMatch(
                        RecipeType.SMELTING,
                        new SimpleInventory(stack),
                        world
                    );
                return optional.isPresent();
            }));*/
	}

   /* private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }*/
}
