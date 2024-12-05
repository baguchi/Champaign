package baguchi.champaign.data.generator;

import baguchi.champaign.Champaign;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class BlockstateGenerator extends BlockStateProvider {

    public BlockstateGenerator(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, Champaign.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }


    public void orientableWithoutSideBlock(Block block) {
        ModelFile model = models().withExistingParent(name(block), ResourceLocation.withDefaultNamespace("block/orientable"))
                .texture("front", blockTexture(block))
                .texture("side", blockTexture(block))
                .texture("top", suffix(blockTexture(block), "_top"));
        simpleBlock(block,
                model);
    }

    public void simpleBlock(Supplier<Block> block) {
        simpleBlock(block.get(), cubeAll(block.get()));
    }

    protected ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    private ResourceLocation suffix(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    protected String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

}
