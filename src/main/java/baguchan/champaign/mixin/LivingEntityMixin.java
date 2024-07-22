package baguchan.champaign.mixin;

import baguchan.champaign.attachment.OwnerAttachment;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "shouldDropLoot", at = @At("HEAD"), cancellable = true)
    public void shoudDropLoot(CallbackInfoReturnable<Boolean> cir) {
        OwnerAttachment ownerAttachment = this.getData(ModAttachments.OWNER);
        if (ownerAttachment.getOwnerID() != null) {
            cir.setReturnValue(false);
        }
    }
}
