package baguchi.champaign;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ChampaignConfig {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        public final ModConfigSpec.BooleanValue enableCampaign;

        public Common(ModConfigSpec.Builder builder) {
            enableCampaign = builder
                    .comment("Enable The Campaign GamePlay. less power and less mining speed. but allay you have")
                    .translation(Champaign.MODID + ".config.enable_campaign")
                    .define("Enable Campaign", true);
        }
    }
}