package baguchi.champaign.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {
    public static final KeyMapping KEY_SUMMON = new KeyMapping("key.champaign.summon", InputConstants.KEY_V, "key.categories.champaign");
    public static final KeyMapping KEY_SUMMON_ALLAY = new KeyMapping("key.champaign.summon_allay", InputConstants.KEY_B, "key.categories.champaign");
    public static final KeyMapping KEY_CALL_ALLAY = new KeyMapping("key.champaign.call_allay", InputConstants.KEY_C, "key.categories.champaign");

}
