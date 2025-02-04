package baguchi.champaign.client;


import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.render.GatherAllayRenderer;
import baguchi.champaign.registry.ModAttachments;
import baguchi.champaign.registry.ModEntities;
import baguchi.champaign.registry.ModItems;
import baguchi.champaign.registry.ModKeyMappings;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
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
        event.register(ModKeyMappings.KEY_SUMMON_ALLAY);
        event.register(ModKeyMappings.KEY_CALL_ALLAY);
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

        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "allay"), (guiGraphics, partialTicks) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Window window = minecraft.getWindow();
            Options options = minecraft.options;
            LocalPlayer player = minecraft.player;
            if (player != null && !options.hideGui) {
                renderAllayOverlay(guiGraphics, minecraft, player.getData(ModAttachments.CHAMPAIGN), partialTicks);
            }
        });
    }


    private static void renderAllayOverlay(GuiGraphics guiGraphics, Minecraft minecraft, ChampaignAttachment attachment, DeltaTracker partialTicks) {
        int x = guiGraphics.guiHeight();
        int y = guiGraphics.guiWidth();
        int i = guiGraphics.guiWidth() / 2;
        int i2 = guiGraphics.guiHeight() - (16 * 2) - 3 - 22;
        HumanoidArm humanoidarm = minecraft.player.getMainArm().getOpposite();
        guiGraphics.pose().pushPose();
        if (humanoidarm == HumanoidArm.LEFT) {

            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.blitSprite(RenderType::guiTexturedOverlay, ALLAY_GUI_TEXTURE, i - 91 - 26, i2, 22, 22);
            String s = String.valueOf(attachment.getAllayCount());
            guiGraphics.drawString(minecraft.font, s, i - 91 - 26 + 19 - 2 - minecraft.font.width(s), i2 + 6 + 3, 16777215, true);
        } else {

            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.blitSprite(RenderType::guiTexturedOverlay, ALLAY_GUI_TEXTURE, i + 91 + 26, i2, 22, 22);
            String s = String.valueOf(attachment.getAllayCount());
            guiGraphics.drawString(minecraft.font, s, i + 91 + 26 + 19 - 2 - minecraft.font.width(s), i2 + 6 + 3, 16777215, true);

        }
        guiGraphics.pose().popPose();
    }
}
