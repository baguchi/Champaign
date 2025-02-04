package baguchi.champaign.data.generator;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class ModItemModels extends ItemModelGenerators {

    public ModItemModels(ItemModelOutput p_387620_, BiConsumer<ResourceLocation, ModelInstance> p_387848_) {
        super(p_387620_, p_387848_);
    }

    @Override
    public void run() {
        //this.generateFlatItem(ModItems.MUSIC_PATTERN.asItem(), ModelTemplates.FLAT_ITEM);
    }
}
