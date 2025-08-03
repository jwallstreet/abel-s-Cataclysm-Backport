package com.github.L_Ender.cataclysm.client.model.entity;

import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class Teddy_Bear_Model extends HierarchicalModel<Teddy_Bear_Entity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public Teddy_Bear_Model(ModelPart root) {
        this.root = root;
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.body.getChild("right_leg");
        this.left_leg = this.body.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Main body - positioned at the base
        PartDefinition body = partdefinition.addOrReplaceChild("body", 
            CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, 20.0F, 0.0F));

        // Head
        PartDefinition head = body.addOrReplaceChild("head", 
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                // Ears
                .texOffs(32, 0)
                .addBox(-4.0F, -6.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0)
                .addBox(1.0F, -6.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                // Snout
                .texOffs(24, 0)
                .addBox(-1.5F, -1.0F, -5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(0.0F, -8.0F, 0.0F));

        // Right arm
        PartDefinition right_arm = body.addOrReplaceChild("right_arm", 
            CubeListBuilder.create()
                .texOffs(28, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(-5.0F, -6.0F, 0.0F));

        // Left arm
        PartDefinition left_arm = body.addOrReplaceChild("left_arm", 
            CubeListBuilder.create()
                .texOffs(40, 16)
                .addBox(-1.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(5.0F, -6.0F, 0.0F));

        // Right leg
        PartDefinition right_leg = body.addOrReplaceChild("right_leg", 
            CubeListBuilder.create()
                .texOffs(0, 30)
                .addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(-2.0F, 0.0F, 0.0F));

        // Left leg
        PartDefinition left_leg = body.addOrReplaceChild("left_leg", 
            CubeListBuilder.create()
                .texOffs(14, 30)
                .addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(2.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Teddy_Bear_Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        // Basic head movement
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        
        // Basic walking animation
        this.right_leg.xRot = (float) Math.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_leg.xRot = (float) Math.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.right_arm.xRot = (float) Math.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.left_arm.xRot = (float) Math.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
    }

    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}