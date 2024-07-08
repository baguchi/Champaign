package baguchan.champaign;

import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.packet.SyncAllayPacket;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Champaign.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getSummonCount(), attachment.getMaxSummonCount()));
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getSummonCount(), attachment.getMaxSummonCount()));
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getSummonCount(), attachment.getMaxSummonCount()));
        }
    }
}
