package baguchan.champaign.data.generator;

import baguchan.champaign.Champaign;
import baguchan.champaign.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper exFileHelper) {
        super(packOutput, lookupProvider, Champaign.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.MUSIC_TABLE.get());
    }
}
