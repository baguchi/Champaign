package baguchan.champaign.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {
    public static final KeyMapping KEY_SUMMON = new KeyMapping("key.champaign.summon", InputConstants.KEY_V, "key.categories.champaign");
    public static final KeyMapping KEY_CALL = new KeyMapping("key.champaign.call", InputConstants.KEY_B, "key.categories.champaign");
}
