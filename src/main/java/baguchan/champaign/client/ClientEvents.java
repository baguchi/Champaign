package baguchan.champaign.client;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.packet.ChangeMusicSlotPacket;
import baguchan.champaign.packet.SummonPacket;
import baguchan.champaign.registry.ModAttachments;
import baguchan.champaign.registry.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static int pressSummonTick;

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (ModKeyMappings.KEY_CHANGE_MUSIC.isDown() && event.getScrollDeltaY() != 0) {
            onMouseScrolled(event.getScrollDeltaY());

            event.setCanceled(true);
        }
    }

    public static boolean onMouseScrolled(double scrollDelta) {
        ChampaignAttachment attachment = Minecraft.getInstance().player.getData(ModAttachments.CHAMPAIGN);
        attachment.cycle(scrollDelta > 0 ? 1 : -1);
        PacketDistributor.sendToServer(new ChangeMusicSlotPacket(scrollDelta > 0 ? 1 : -1));
        return true;
    }


    @SubscribeEvent
    public static void onPlayerPostTick(PlayerTickEvent.Post event) {

        if (ModKeyMappings.KEY_SUMMON.isDown()) {
            if (pressSummonTick <= 0) {
                pressSummonTick = 20;
                PacketDistributor.sendToServer(new SummonPacket());
            }
        }

        if (pressSummonTick > 0) {
            --pressSummonTick;
        }
    }


}
