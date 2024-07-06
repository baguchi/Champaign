package baguchan.champaign.packet;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SyncAllayPacket implements CustomPacketPayload, IPayloadHandler<SyncAllayPacket> {

    public static final StreamCodec<FriendlyByteBuf, SyncAllayPacket> STREAM_CODEC = CustomPacketPayload.codec(
            SyncAllayPacket::write, SyncAllayPacket::new
    );
    public static final CustomPacketPayload.Type<SyncAllayPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "sync_allay"));

    private final int entityId;

    private final int allayCount;
    private final int allayMaxCount;

    public SyncAllayPacket(LivingEntity entity, int allayCount, int allayMaxCount) {
        this.entityId = entity.getId();
        this.allayCount = allayCount;
        this.allayMaxCount = allayMaxCount;
    }

    public SyncAllayPacket(int id, int allayCount, int allayMaxCount) {
        this.entityId = id;
        this.allayCount = allayCount;
        this.allayMaxCount = allayMaxCount;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.allayCount);
        buffer.writeInt(this.allayMaxCount);
    }

    public SyncAllayPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public void handle(SyncAllayPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = (Minecraft.getInstance()).player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof Player player) {
                ChampaignAttachment cap = player.getData(ModAttachments.CHAMPAIGN);
                cap.setAllayCount(this.allayCount, player);
                cap.setMaxAllayCount(this.allayMaxCount, player);
            }
        });

    }
}