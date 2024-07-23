package baguchan.champaign.data.generator;

import baguchan.champaign.Champaign;
import baguchan.champaign.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

import static baguchan.champaign.Champaign.prefix;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Champaign.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MUSIC_PATTERN.get());
    }

    private ItemModelBuilder toBlock(Supplier<? extends Block> b) {
        return toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b.get()).getPath());
    }

    private ItemModelBuilder toBlockModel(Supplier<? extends Block> b, String model) {
        return toBlockModel(b, prefix("block/" + model));
    }

    private ItemModelBuilder toBlockModel(Supplier<? extends Block> b, ResourceLocation model) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(b.get()).getPath(), model);
    }

    public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block) {
        return itemBlockFlat(block, blockName(block));
    }

    public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block, String name) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + name));
    }

    public ItemModelBuilder egg(Supplier<Item> item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), mcLoc("item/template_spawn_egg"));
    }

    public String blockName(Supplier<? extends Block> block) {
        return BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
    }

    private ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    public ResourceLocation itemPath(Supplier<? extends ItemLike> item) {
        return BuiltInRegistries.ITEM.getKey(item.get().asItem());
    }
}
