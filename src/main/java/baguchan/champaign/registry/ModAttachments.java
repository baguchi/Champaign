package baguchan.champaign.registry;

import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.attachment.OwnerAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Champaign.MODID);

    public static final Supplier<AttachmentType<ChampaignAttachment>> CHAMPAIGN = ATTACHMENT_TYPES.register(
            "champaign", () -> AttachmentType.serializable(ChampaignAttachment::new).copyOnDeath().build());

    public static final Supplier<AttachmentType<OwnerAttachment>> OWNER = ATTACHMENT_TYPES.register(
            "owner", () -> AttachmentType.serializable(OwnerAttachment::new).copyOnDeath().build());

}