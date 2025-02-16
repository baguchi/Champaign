package baguchi.champaign.client;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.music.MusicSummon;
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

    private static final ResourceLocation TEXTURE = new ResourceLocation(Champaign.MODID, "gui/sprites/hud/hotbar");
    private static final ResourceLocation TEXTURE_SELECT = new ResourceLocation(Champaign.MODID, "gui/sprites/hud/hotbar_select");


    private static void renderSlot(GuiGraphics graphics, int pX, int pY, MusicSummon musicSummon) {
        if (musicSummon != null) {
            Entity entity = musicSummon.getEntityType().create(Minecraft.getInstance().level);
            if (entity instanceof LivingEntity livingEntity) {
                graphics.pose().pushPose();

                InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, pX + 2, pY + 2, pX + 19, pY + 19, 25, livingEntity);

                graphics.pose().popPose();
                graphics.pose().pushPose();

                String s = String.valueOf(musicSummon.summonCost());
                graphics.pose().translate(0.0F, 0.0F, 200.0F);
                graphics.drawString(Minecraft.getInstance().font, s, pX + 22 - 2 - Minecraft.getInstance().font.width(s), pY + 12, 16777215, true);

                graphics.pose().popPose();
            }
        }
    }

    public static void renderEntityContent(GuiGraphics graphics, int screenWidth, int screenHeight) {

        if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
            ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
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
                    renderSlot(graphics, jx, py, list.get(i).value());
                    graphics.blit(TEXTURE, jx, py, 0, 0, 22, 22);

                } else {
                    renderSlot(graphics, jx, py, null);
                    graphics.blit(TEXTURE, jx, py, 0, 0, 22, 22);
                }
            }
            if (selected < slots) {
                int jx = px - 10 - selected * 20;
                graphics.blit(TEXTURE_SELECT, jx - 1, py - 1, 0, 0, 24, 24);
            }


            if (!list.isEmpty() && list.size() > selected) {
                int jx = px;
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
        int nx = (screenWidth - (fontWidth / 2));
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