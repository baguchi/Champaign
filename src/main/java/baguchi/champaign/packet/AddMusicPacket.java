package baguchi.champaign.packet;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.render.toast.LearningToast;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeJsonWithCodec(MusicSummon.CODEC, this.musicSummon);
        buffer.writeBoolean(this.makeToast);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public AddMusicPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readJsonWithCodec(MusicSummon.CODEC), buffer.readBoolean());
    }

    @Override
    public void handle(AddMusicPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof Player player) {
                ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
                attachment.addMusicList(Champaign.registryAccess().lookupOrThrow(MusicSummon.REGISTRY_KEY).wrapAsHolder(musicSummon), player);
                if (player == Minecraft.getInstance().player && makeToast) {
                    Minecraft.getInstance().getToastManager().addToast(new LearningToast(Component.translatable("toast.champaign.learning"), Component.translatable("toast.champaign.learn_entity", musicSummon.entityType().getDescription())));
                }
            }
        });
    }
}