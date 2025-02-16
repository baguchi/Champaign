package baguchi.champaign.mixin;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.OwnerAttachment;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, remap = false)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "shouldDropLoot", at = @At("HEAD"), cancellable = true)
    public void shoudDropLoot(CallbackInfoReturnable<Boolean> cir) {
        OwnerAttachment ownerAttachment = this.getCapability(Champaign.OWNER_CAPABILITY).orElse(new OwnerAttachment());
        if (ownerAttachment.getOwnerID() != null) {
            cir.setReturnValue(false);
        }
    }
}
