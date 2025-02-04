package baguchi.champaign.client.render;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AllayRenderState;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GatherAllayRenderer<T extends baguchi.champaign.entity.AbstractWorkerAllay> extends MobRenderer<T, AllayRenderState, baguchi.champaign.client.WorkerAllayModel<AllayRenderState>> {
    private static final ResourceLocation ALLAY_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/allay/allay.png");

    public GatherAllayRenderer(EntityRendererProvider.Context p_234551_) {
        super(p_234551_, new baguchi.champaign.client.WorkerAllayModel<>(p_234551_.bakeLayer(ModelLayers.ALLAY)), 0.4F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    protected int getBlockLightLevel(T p_234560_, BlockPos p_234561_) {
        return 15;
    }

    @Override
    public AllayRenderState createRenderState() {
        return new AllayRenderState();
    }

    public void extractRenderState(T p_364238_, AllayRenderState p_364959_, float p_364487_) {
        super.extractRenderState(p_364238_, p_364959_, p_364487_);
        ArmedEntityRenderState.extractArmedEntityRenderState(p_364238_, p_364959_, this.itemModelResolver);
    }

    @Override
    public ResourceLocation getTextureLocation(AllayRenderState p_368654_) {
        return ALLAY_TEXTURE;
    }
}