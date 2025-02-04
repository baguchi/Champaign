package baguchi.champaign.data.generator;

import baguchi.champaign.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CraftingGenerator extends RecipeProvider {

    public CraftingGenerator(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> p_323846_) {
        super(p_248933_, p_323846_);
    }

    @Override
    protected void buildRecipes(RecipeOutput p_301172_) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, new ItemStack(ModItems.LUTE.asItem()))
                .define('S', Items.STRING)
                .define('W', ItemTags.PLANKS)
                .define('A', Items.AMETHYST_SHARD)
                .pattern(" WS")
                .pattern("WAW")
                .pattern("SW ")
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(p_301172_);
    }
}
