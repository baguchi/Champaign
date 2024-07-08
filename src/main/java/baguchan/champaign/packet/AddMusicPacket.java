package baguchan.champaign.packet;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.music.MusicSummon;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class AddMusicPacket implements CustomPacketPayload, IPayloadHandler<AddMusicPacket> {

    public static final StreamCodec<FriendlyByteBuf, AddMusicPacket> STREAM_CODEC = CustomPacketPayload.codec(
            AddMusicPacket::write, AddMusicPacket::new
    );
    public static final CustomPacketPayload.Type<AddMusicPacket> TYPE = new Type<>(Champaign.prefix("add_music"));


    private int entityId;
    private MusicSummon musicSummon;


    public AddMusicPacket(int id, MusicSummon musicSummon) {
        this.entityId = id;
        this.musicSummon = musicSummon;
    }

    public AddMusicPacket(Entity entity, MusicSummon musicSummon) {
        this.entityId = entity.getId();
        this.musicSummon = musicSummon;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeJsonWithCodec(MusicSummon.CODEC, this.musicSummon);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public AddMusicPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readJsonWithCodec(MusicSummon.CODEC));
    }

    @Override
    public void handle(AddMusicPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof Player player) {
                ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
                attachment.addMusicList(Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).wrapAsHolder(musicSummon), player);
            }
        });
    }
}