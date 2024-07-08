package baguchan.champaign.data.generator;

import baguchan.champaign.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.MUSIC_TABLE.get(), 1)
                .pattern("FI")
                .pattern("PP")
                .pattern("PP")
                .define('P', ItemTags.PLANKS)
                .define('F', Items.FEATHER)
                .define('I', Items.BLACK_DYE)
                .unlockedBy("has_item", has(Items.FEATHER))
                .save(recipeOutput);
    }
}
