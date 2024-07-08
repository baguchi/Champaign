package baguchan.champaign.client;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.music.MusicSummon;
import baguchan.champaign.registry.ModAttachments;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RenderHelper {
    private static Gui ingameGui;

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "hud/hotbar");
    private static final ResourceLocation TEXTURE_SELECT = ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "hud/hotbar_select");


    private static void renderSlot(GuiGraphics graphics, int pX, int pY, EntityType<?> entityType) {
        if (entityType != null) {
            Entity entity = entityType.create(Minecraft.getInstance().level);
            if (entity instanceof LivingEntity livingEntity) {
                InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, pX + 2, pY + 2, pX + 19, pY + 19, 25, 0.0625F, 0F, 0F, livingEntity);
            }
        }
    }

    public static void renderEntityContent(GuiGraphics graphics, int screenWidth, int screenHeight) {

        if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
            ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
            ///gui.setupOverlayRenderState(true, false);
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();

            int selected = attachment.getMusicIndex();
            List<Holder<MusicSummon>> list = attachment.getMusicList();
            int slots = list.size();
            int uWidth = slots * 20 + 2;
            int px = screenWidth / 2;
            int py = screenHeight / 2;

            px += 0;
            py += 0;
            int i1 = 1;
            for (int i = 0; i < slots; ++i) {
                int jx = px - 10 - i * 20;
                if (!list.isEmpty() && list.size() > i) {
                    renderSlot(graphics, jx, py, list.get(i).value().entityType());
                    graphics.blitSprite(TEXTURE, jx, py, 22, 22);

                } else {
                    renderSlot(graphics, jx, py, null);
                    graphics.blitSprite(TEXTURE, jx, py, 22, 22);
                }
            }
            if (selected < slots) {
                int jx = px - 10 - selected * 20;
                graphics.blitSprite(TEXTURE_SELECT, jx - 2, py - 2, 24, 24);
            }


            if (!list.isEmpty() && list.size() > selected) {
                int jx = px - 10 - selected * 20;
                MusicSummon select = list.get(selected).value();
                drawHighlight(graphics, jx, py, select.entityType());
            }


            poseStack.popPose();

        }
    }


    protected static void drawHighlight(GuiGraphics graphics, int screenWidth, int py, EntityType<?> selectedEntity) {
        int l;

        MutableComponent mutablecomponent = Component.empty().append(selectedEntity.getDescription());
        Component highlightTip = mutablecomponent;
        int fontWidth = Minecraft.getInstance().font.width(highlightTip);
        int nx = (screenWidth - fontWidth) / 2;
        int ny = py - 19;

        l = 255;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.fill(nx, ny, nx + fontWidth + 2, ny + 9 + 2, Minecraft.getInstance().options.getBackgroundColor(0));
        graphics.drawString(Minecraft.getInstance().font, highlightTip, nx, ny, 0xFFFFFF + (l << 24));
        RenderSystem.disableBlend();
    }


    public static void renderSlot(GuiGraphics guiGraphics, float partialTick) {
        renderEntityContent(guiGraphics,
                Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                Minecraft.getInstance().getWindow().getGuiScaledHeight());
    }

}