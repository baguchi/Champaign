package baguchan.champaign;

import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.packet.AddMusicPacket;
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
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }
}
