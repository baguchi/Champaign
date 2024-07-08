package baguchan.champaign.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.UUID;

public class OwnerAttachment implements INBTSerializable<CompoundTag> {
    private UUID ownerID;

    public UUID getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(UUID ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();

        if (this.ownerID != null) {
            nbt.putUUID("OwnerId", this.ownerID);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("OwnerId")) {
            this.ownerID = nbt.getUUID("OwnerId");
        }
    }
}