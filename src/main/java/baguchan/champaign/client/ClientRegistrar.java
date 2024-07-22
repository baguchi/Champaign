package baguchan.champaign.client;


import baguchan.champaign.Champaign;
import baguchan.champaign.client.render.GatherAllayRenderer;
import baguchan.champaign.registry.ModEntities;
import baguchan.champaign.registry.ModItems;
import baguchan.champaign.registry.ModKeyMappings;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {

    private static final ResourceLocation ALLAY_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "allay_hotbar");


    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GATHER_ALLAY.get(), GatherAllayRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.KEY_SUMMON);
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "entity_slot"), (guiGraphics, partialTicks) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Window window = minecraft.getWindow();
            LocalPlayer player = minecraft.player;
            if (player != null && player.getUseItem().is(ModItems.LUTE.get())) {
                RenderHelper.renderEntityContent(guiGraphics, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }
        });
    }

}
