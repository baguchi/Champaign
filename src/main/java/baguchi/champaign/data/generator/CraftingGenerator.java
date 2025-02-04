package baguchi.champaign.data.generator;

import baguchi.champaign.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(HolderLookup.Provider packOutput, RecipeOutput lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes() {
        HolderLookup<Item> itemHolderLookup = this.registries.lookupOrThrow(Registries.ITEM);
        ShapedRecipeBuilder.shaped(itemHolderLookup, RecipeCategory.TOOLS, new ItemStack(ModItems.LUTE.asItem()))
                .define('S', Items.STRING)
                .define('W', ItemTags.PLANKS)
                .define('A', Items.AMETHYST_SHARD)
                .pattern(" WS")
                .pattern("WAW")
                .pattern("SW ")
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(this.output);
    }
}
