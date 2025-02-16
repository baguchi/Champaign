package baguchi.champaign.client.render;

import baguchi.champaign.client.WorkerAllayModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GatherAllayRenderer<T extends baguchi.champaign.entity.AbstractWorkerAllay> extends MobRenderer<T, WorkerAllayModel<T>> {
    private static final ResourceLocation ALLAY_TEXTURE = new ResourceLocation("textures/entity/allay/allay.png");

    public GatherAllayRenderer(EntityRendererProvider.Context p_234551_) {
        super(p_234551_, new baguchi.champaign.client.WorkerAllayModel<>(p_234551_.bakeLayer(ModelLayers.ALLAY)), 0.4F);
        this.addLayer(new ItemInHandLayer<>(this, p_234551_.getItemInHandRenderer()));
    }

    @Override
    protected int getBlockLightLevel(T p_234560_, BlockPos p_234561_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(T p_368654_) {
        return ALLAY_TEXTURE;
    }
}