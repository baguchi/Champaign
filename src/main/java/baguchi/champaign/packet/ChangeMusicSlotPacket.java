package baguchi.champaign.packet;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeMusicSlotPacket {

    private int move;

    public ChangeMusicSlotPacket(int move) {
        this.move = move;
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.move);
    }

    public static ChangeMusicSlotPacket deserialize(FriendlyByteBuf buffer) {
        return new ChangeMusicSlotPacket(
                buffer.readInt()
        );
    }

    public static void handle(ChangeMusicSlotPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = contextSupplier.get().getSender();
            ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
            attachment.cycle(message.move);
        });
    }
}