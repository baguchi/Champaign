package baguchi.champaign.item;

import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.registry.ModAttachments;
import baguchi.champaign.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MusicPatternItem extends Item {
    public MusicPatternItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.has(ModDataComponents.MUSIC_TYPE)) {
            if (!level.isClientSide) {
                ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
                if (!attachment.getMusicList().contains(itemstack.get(ModDataComponents.MUSIC_TYPE))) {
                    attachment.addMusicList(itemstack.get(ModDataComponents.MUSIC_TYPE), player);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            itemstack.consume(1, player);
            return InteractionResultHolder.success(itemstack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339613_, List<Component> p_43096_, TooltipFlag p_43097_) {
        super.appendHoverText(stack, p_339613_, p_43096_, p_43097_);

        if (stack.has(ModDataComponents.MUSIC_TYPE)) {
            @Nullable Holder<MusicSummon> musicSummon = stack.get(ModDataComponents.MUSIC_TYPE);

            p_43096_.add(musicSummon.value().getEntityType().getDescription().copy().withStyle(ChatFormatting.AQUA));
        }
    }
}
