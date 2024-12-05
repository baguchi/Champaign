package baguchi.champaign.client.render.toast;

import baguchi.champaign.registry.ModItems;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class LearningToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");
    private final Component title;
    private final Component description;
    private long lastChanged;
    private boolean changed;
    private boolean playedSound;
    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;

    public LearningToast(Component title, Component description) {

        this.title = title;
        this.description = description;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager p_363415_, long p_363939_) {
        if (!this.playedSound && p_363939_ > 0L) {
            this.playedSound = true;
            p_363415_.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));

        }

        this.wantedVisibility = (double) p_363939_ >= 5000.0 * p_363415_.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE
                : Toast.Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long timeSinceLastVisible) {
        guiGraphics.blitSprite(RenderType::guiTextured, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        List<FormattedCharSequence> list = font.split(this.description, 125);
        int i = 16776960;
        if (list.size() == 1) {
            guiGraphics.drawString(font, this.title, 32, 7, -13423317, false);
            guiGraphics.drawString(font, this.description, 32, 18, -724497, false);
        } else {
            int j = 1500;
            float f = 300.0F;
            if (timeSinceLastVisible < 1500L) {
                int k = Mth.floor(Mth.clamp((float) (1500L - timeSinceLastVisible) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                guiGraphics.drawString(font, this.title, 30, 11, i | k, false);
            } else {
                int i1 = Mth.floor(Mth.clamp((float) (timeSinceLastVisible - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int l = this.height() / 2 - list.size() * 9 / 2;

                for (FormattedCharSequence formattedcharsequence : list) {
                    guiGraphics.drawString(font, formattedcharsequence, 30, l, 16777215 | i1, false);
                    l += 9;
                }
            }

            guiGraphics.renderFakeItem(ModItems.LUTE.get().getDefaultInstance(), 8, 8);
        }
    }
}