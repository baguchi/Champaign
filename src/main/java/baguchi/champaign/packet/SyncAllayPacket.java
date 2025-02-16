package baguchi.champaign.packet;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAllayPacket {

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

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.allayCount);
        buffer.writeInt(this.allayMaxCount);
    }

    public static SyncAllayPacket deserialize(FriendlyByteBuf buffer) {
        return new SyncAllayPacket(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }


    public static void handle(SyncAllayPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Entity entity = (Minecraft.getInstance()).player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof Player player) {
                baguchi.champaign.attachment.ChampaignAttachment cap = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
                cap.setAllayCount(message.allayCount, player);
                cap.setMaxAllayCount(message.allayMaxCount, player);
            }
        });

    }
}