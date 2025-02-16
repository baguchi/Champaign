package baguchi.champaign.packet;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CallPacket {


    public CallPacket() {
    }

    public void serialize(FriendlyByteBuf buffer) {
    }

    public static CallPacket deserialize(FriendlyByteBuf buffer) {
        return new CallPacket();
    }

    public static void handle(CallPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = contextSupplier.get().getSender();
            if (player instanceof ServerPlayer serverPlayer) {
                ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
                attachment.callAllay(serverPlayer);
            }
        });
    }
}