package baguchan.champaign.packet;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class ChangeMusicSlotPacket implements CustomPacketPayload, IPayloadHandler<ChangeMusicSlotPacket> {

    public static final StreamCodec<FriendlyByteBuf, ChangeMusicSlotPacket> STREAM_CODEC = CustomPacketPayload.codec(
            ChangeMusicSlotPacket::write, ChangeMusicSlotPacket::new
    );
    public static final CustomPacketPayload.Type<ChangeMusicSlotPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "change_music"));

    private int move;

    public ChangeMusicSlotPacket(int move) {
        this.move = move;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.move);
    }

    public ChangeMusicSlotPacket(FriendlyByteBuf buffer) {
        this(
                buffer.readInt()
        );
    }

    public void handle(ChangeMusicSlotPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
            attachment.cycle(this.move);
        });
    }
}