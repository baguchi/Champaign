package baguchi.champaign.data.generator;

import baguchi.champaign.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class CraftingGenerator extends RecipeProvider {

    public CraftingGenerator(PackOutput p_248933_) {
        super(p_248933_);
    }


    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.LUTE.get())
                .define('S', Items.STRING)
                .define('W', ItemTags.PLANKS)
                .define('A', Items.AMETHYST_SHARD)
                .pattern(" WS")
                .pattern("WAW")
                .pattern("SW ")
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(consumer);
    }
}
