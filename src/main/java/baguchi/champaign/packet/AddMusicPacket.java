package baguchi.champaign.packet;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.render.toast.LearningToast;
import baguchi.champaign.music.MusicSummon;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddMusicPacket {


    private int entityId;
    private MusicSummon musicSummon;
    private boolean makeToast;


    public AddMusicPacket(int id, MusicSummon musicSummon, boolean toast) {
        this.entityId = id;
        this.musicSummon = musicSummon;
        this.makeToast = toast;
    }

    public AddMusicPacket(Entity entity, MusicSummon musicSummon, boolean toast) {
        this.entityId = entity.getId();
        this.musicSummon = musicSummon;
        this.makeToast = toast;
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeJsonWithCodec(MusicSummon.CODEC, this.musicSummon);
        buffer.writeBoolean(this.makeToast);
    }

    public static AddMusicPacket deserialize(FriendlyByteBuf buffer) {
        return new AddMusicPacket(buffer.readInt(), buffer.readJsonWithCodec(MusicSummon.CODEC), buffer.readBoolean());
    }

    public static void handle(AddMusicPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof Player player) {
                ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
                attachment.addMusicList(Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).wrapAsHolder(message.musicSummon), player);
                if (player == Minecraft.getInstance().player && message.makeToast) {
                    Minecraft.getInstance().getToasts().addToast(new LearningToast(Component.translatable("toast.champaign.learning"), Component.translatable("toast.champaign.learn_entity", message.musicSummon.entityType().getDescription())));
                }
            }
        });
    }
}