package baguchi.champaign.data.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(HolderLookup.Provider packOutput, RecipeOutput lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes() {

    }
}
