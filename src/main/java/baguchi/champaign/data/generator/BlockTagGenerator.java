package baguchi.champaign.data.generator;

import baguchi.champaign.Champaign;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Champaign.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        //tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.MUSIC_TABLE.get());
    }
}
