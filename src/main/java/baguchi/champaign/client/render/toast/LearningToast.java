package baguchi.champaign.client.render.toast;

import baguchi.champaign.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class LearningToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("textures/gui/toasts.png");
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
    public Toast.Visibility render(GuiGraphics p_281813_, ToastComponent p_282243_, long p_282604_) {
        p_281813_.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());

        List<FormattedCharSequence> list = p_282243_.getMinecraft().font.split(title, 125);
        int i = 16746751;
        if (list.size() == 1) {
            p_281813_.drawString(p_282243_.getMinecraft().font, description, 30, 7, -11534256, false);
            p_281813_.drawString(p_282243_.getMinecraft().font, list.get(0), 30, 18, -11534256, false);
        } else {
            int j = 1500;
            float f = 300.0F;
            if (p_282604_ < 1500L) {
                int k = Mth.floor(Mth.clamp((float) (1500L - p_282604_) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                p_281813_.drawString(p_282243_.getMinecraft().font, description, 30, 11, i | k, false);
            } else {
                int i1 = Mth.floor(Mth.clamp((float) (p_282604_ - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int l = this.height() / 2 - list.size() * 9 / 2;

                for (FormattedCharSequence formattedcharsequence : list) {
                    p_281813_.drawString(p_282243_.getMinecraft().font, formattedcharsequence, 30, l, -11534256 | i1, false);
                    l += 9;
                }
                }
            }

        if (!this.playedSound && p_282604_ > 0L) {
            this.playedSound = true;
            p_282243_.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));

        }

        p_281813_.renderFakeItem(ModItems.MUSIC_PATTERN.get().getDefaultInstance(), 8, 8);
        return (double) p_282604_ >= 5000.0 * p_282243_.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}