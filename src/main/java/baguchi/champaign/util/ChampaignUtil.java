package baguchi.champaign.util;

import baguchi.champaign.music.MusicSummon;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ChampaignUtil {

    public static final String TAG_MUSIC = "Music";

    public static ItemStack makeMusic(ItemStack stack, Holder<MusicSummon> musicSummonHolder) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putString(TAG_MUSIC, musicSummonHolder.unwrapKey().get().location().toString());

        return stack;
    }
}
