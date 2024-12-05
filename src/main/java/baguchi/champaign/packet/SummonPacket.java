package baguchi.champaign.packet;

import baguchi.bagus_lib.util.client.AnimationUtil;
import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.registry.ModAnimations;
import baguchi.champaign.registry.ModAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SummonPacket implements CustomPacketPayload, IPayloadHandler<SummonPacket> {

    public static final StreamCodec<FriendlyByteBuf, SummonPacket> STREAM_CODEC = CustomPacketPayload.codec(
            SummonPacket::write, SummonPacket::new
    );
    public static final Type<SummonPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "summon"));


    public SummonPacket() {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
    }

    public SummonPacket(FriendlyByteBuf buffer) {
        this(
        );
    }

    public void handle(SummonPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
                attachment.summonEntity(serverPlayer);
                AnimationUtil.sendAnimation(serverPlayer, ModAnimations.PLAYING_LUTE);
            }
        });
    }
}