package baguchan.champaign.client.render;

import baguchan.champaign.client.WorkerAllayModel;
import baguchan.champaign.entity.AbstractWorkerAllay;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GatherAllayRenderer<T extends AbstractWorkerAllay> extends MobRenderer<T, WorkerAllayModel<T>> {
    private static final ResourceLocation ALLAY_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/allay/allay.png");

    public GatherAllayRenderer(EntityRendererProvider.Context p_234551_) {
        super(p_234551_, new WorkerAllayModel(p_234551_.bakeLayer(ModelLayers.ALLAY)), 0.4F);
        this.addLayer(new ItemInHandLayer<>(this, p_234551_.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(T p_234558_) {
        return ALLAY_TEXTURE;
    }

    protected int getBlockLightLevel(T p_234560_, BlockPos p_234561_) {
        return 15;
    }
}