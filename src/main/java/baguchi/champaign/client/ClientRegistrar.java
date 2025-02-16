package baguchi.champaign.client;


import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.render.GatherAllayRenderer;
import baguchi.champaign.registry.ModEntities;
import baguchi.champaign.registry.ModItems;
import baguchi.champaign.registry.ModKeyMappings;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {

    private static final ResourceLocation ALLAY_GUI_TEXTURE = new ResourceLocation(Champaign.MODID, "textures/gui/sprites/allay_hotbar.png");


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
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("entity_slot", (gui, guiGraphics, tick, wight, height) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Window window = minecraft.getWindow();
            LocalPlayer player = minecraft.player;
            if (player != null && player.getUseItem().is(ModItems.LUTE.get())) {
                RenderHelper.renderEntityContent(guiGraphics, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }
        });

        event.registerAboveAll("allay", (gui, guiGraphics, tick, wight, height) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Window window = minecraft.getWindow();
            Options options = minecraft.options;
            LocalPlayer player = minecraft.player;
            if (player != null && !options.hideGui) {
                renderAllayOverlay(guiGraphics, minecraft, player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment()), tick);
            }
        });
    }


    private static void renderAllayOverlay(GuiGraphics guiGraphics, Minecraft minecraft, ChampaignAttachment attachment, float partialTicks) {
        int x = guiGraphics.guiHeight();
        int y = guiGraphics.guiWidth();
        int i = guiGraphics.guiWidth() / 2;
        int i2 = guiGraphics.guiHeight() - (16 * 2) - 3 - 22;
        HumanoidArm humanoidarm = minecraft.player.getMainArm().getOpposite();
        guiGraphics.pose().pushPose();
        if (humanoidarm == HumanoidArm.LEFT) {

            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.blit(ALLAY_GUI_TEXTURE, i - 91 - 26, i2, 0, 0, 22, 22, 22, 22);
            String s = String.valueOf(attachment.getAllayCount());
            guiGraphics.drawString(minecraft.font, s, i - 91 - 26 + 19 - 2 - minecraft.font.width(s), i2 + 6 + 3, 16777215, true);
        } else {

            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.blit(ALLAY_GUI_TEXTURE, i + 91 + 26, i2, 0, 0, 22, 22, 22, 22);
            String s = String.valueOf(attachment.getAllayCount());
            guiGraphics.drawString(minecraft.font, s, i + 91 + 26 + 19 - 2 - minecraft.font.width(s), i2 + 6 + 3, 16777215, true);

        }
        guiGraphics.pose().popPose();
    }
}
