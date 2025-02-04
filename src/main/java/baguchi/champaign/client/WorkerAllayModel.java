package baguchi.champaign.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.AllayRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class WorkerAllayModel<T extends AllayRenderState> extends EntityModel<T> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_wing;
    private final ModelPart left_wing;
    private static final float FLYING_ANIMATION_X_ROT = ((float) Math.PI / 4F);
    private static final float MAX_HAND_HOLDING_ITEM_X_ROT_RAD = -1.134464F;
    private static final float MIN_HAND_HOLDING_ITEM_X_ROT_RAD = (-(float) Math.PI / 3F);

    public WorkerAllayModel(ModelPart p_233312_) {
        super(p_233312_, RenderType::entityTranslucent);
        this.root = p_233312_.getChild("root");
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_wing = this.body.getChild("right_wing");
        this.left_wing = this.body.getChild("left_wing");
    }

    public void setupAnim(T p_365028_) {
        super.setupAnim(p_365028_);
        float f = p_365028_.walkAnimationSpeed;
        float f1 = p_365028_.walkAnimationPos;
        float f2 = p_365028_.ageInTicks * 20.0F * (float) (Math.PI / 180.0) + f1;
        float f3 = Mth.cos(f2) * (float) Math.PI * 0.15F + f;
        float f4 = p_365028_.ageInTicks * 9.0F * (float) (Math.PI / 180.0);
        float f5 = Math.min(f / 0.3F, 1.0F);
        float f6 = 1.0F - f5;
        float f7 = p_365028_.holdingAnimationProgress;
        if (p_365028_.isDancing) {
            float f8 = p_365028_.ageInTicks * 8.0F * (float) (Math.PI / 180.0) + f;
            float f9 = Mth.cos(f8) * 16.0F * (float) (Math.PI / 180.0);
            float f10 = p_365028_.spinningProgress;
            float f11 = Mth.cos(f8) * 14.0F * (float) (Math.PI / 180.0);
            float f12 = Mth.cos(f8) * 30.0F * (float) (Math.PI / 180.0);
            this.root.yRot = p_365028_.isSpinning ? (float) (Math.PI * 4) * f10 : this.root.yRot;
            this.root.zRot = f9 * (1.0F - f10);
            this.head.yRot = f12 * (1.0F - f10);
            this.head.zRot = f11 * (1.0F - f10);
        } else {
            this.head.xRot = p_365028_.xRot * (float) (Math.PI / 180.0);
            this.head.yRot = p_365028_.yRot * (float) (Math.PI / 180.0);
        }

        this.right_wing.xRot = 0.43633232F * (1.0F - f5);
        this.right_wing.yRot = (float) (-Math.PI / 4) + f3;
        this.left_wing.xRot = 0.43633232F * (1.0F - f5);
        this.left_wing.yRot = (float) (Math.PI / 4) - f3;
        this.body.xRot = f5 * (float) (Math.PI / 4);
        float f13 = f7 * Mth.lerp(f5, (float) (-Math.PI / 3), -1.134464F);
        this.root.y = this.root.y + (float) Math.cos((double) f4) * 0.25F * f6;
        this.right_arm.xRot = f13;
        this.left_arm.xRot = f13;
        float f14 = f6 * (1.0F - f7);
        float f15 = 0.43633232F - Mth.cos(f4 + (float) (Math.PI * 3.0 / 2.0)) * (float) Math.PI * 0.075F * f14;
        this.left_arm.zRot = -f15;
        this.right_arm.zRot = f15;
        this.right_arm.yRot = 0.27925268F * f7;
        this.left_arm.yRot = -0.27925268F * f7;
    }

    @Override
    public void translateToHand(HumanoidArm p_233322_, PoseStack p_233323_) {
        float f = 1.0F;
        float f1 = 3.0F;
        this.root.translateAndRotate(p_233323_);
        this.body.translateAndRotate(p_233323_);
        p_233323_.translate(0.0F, 0.0625F, 0.1875F);
        p_233323_.mulPose(Axis.XP.rotation(this.right_arm.xRot));
        p_233323_.scale(0.7F, 0.7F, 0.7F);
        p_233323_.translate(0.0625F, 0.0F, 0.0F);
    }
}
