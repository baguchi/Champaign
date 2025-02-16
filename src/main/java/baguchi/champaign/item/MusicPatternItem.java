package baguchi.champaign.item;

import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.util.ChampaignUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import java.util.Optional;

public class MusicPatternItem extends Item {
    public MusicPatternItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getTag() != null && itemstack.getTag().contains(ChampaignUtil.TAG_MUSIC)) {
            if (!level.isClientSide) {
                ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
                Optional<Holder.Reference<MusicSummon>> music = Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).getHolder(ResourceKey.create(MusicSummon.REGISTRY_KEY, ResourceLocation.tryParse(itemstack.getTag().getString(ChampaignUtil.TAG_MUSIC))));
                if (music.isPresent() && !attachment.getMusicList().contains(music.get())) {
                    attachment.addMusicList(music.get(), player);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            itemstack.shrink(1);
            return InteractionResultHolder.success(itemstack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(itemstack, p_41422_, p_41423_, p_41424_);

        if (itemstack.getTag() != null && itemstack.getTag().contains(ChampaignUtil.TAG_MUSIC)) {
            Optional<Holder.Reference<MusicSummon>> music = Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).getHolder(ResourceKey.create(MusicSummon.REGISTRY_KEY, ResourceLocation.tryParse(itemstack.getTag().getString(ChampaignUtil.TAG_MUSIC))));
            if (music.isPresent()) {
                p_41423_.add(music.get().value().getEntityType().getDescription().copy().withStyle(ChatFormatting.AQUA));
            }
        }
    }

}
