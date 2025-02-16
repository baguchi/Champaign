package baguchi.champaign.packet;

import bagu_chan.bagus_lib.util.client.AnimationUtil;
import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.registry.ModAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SummonPacket {



    public SummonPacket() {
    }


    public void serialize(FriendlyByteBuf buffer) {
    }

    public static SummonPacket deserialize(FriendlyByteBuf buffer) {
        return new SummonPacket(
        );
    }

    public static void handle(SummonPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = contextSupplier.get().getSender();
            if (player instanceof ServerPlayer serverPlayer) {
                ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
                attachment.summonEntity(serverPlayer);
                AnimationUtil.sendAnimation(serverPlayer, ModAnimations.PLAYING_LUTE);
            }
        });
    }
}