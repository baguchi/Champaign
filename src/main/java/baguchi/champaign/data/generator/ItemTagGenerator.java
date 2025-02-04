package baguchi.champaign.data.generator;

import baguchi.champaign.Champaign;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookupCompletableFuture) {
        super(packOutput, lookupProvider, tagLookupCompletableFuture, Champaign.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
