package com.github.L_Ender.cataclysm.client.render.blockentity;


import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.blockentities.Door_Of_Seal_BlockEntity;
import com.github.L_Ender.cataclysm.blocks.Door_of_Seal_Block;
import com.github.L_Ender.cataclysm.client.model.block.Door_Of_Seal_Model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class Door_Of_Seal_Renderer implements BlockEntityRenderer<Door_Of_Seal_BlockEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Cataclysm.MODID,"textures/block/door_of_seal.png");
    private static final Door_Of_Seal_Model MODEL = new Door_Of_Seal_Model();

    public Door_Of_Seal_Renderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public boolean shouldRenderOffScreen(Door_Of_Seal_BlockEntity p_112138_) {
        return true;
    }

    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(Door_Of_Seal_BlockEntity entity, Vec3 p_173532_) {
        return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(p_173532_.multiply(1.0D, 0.0D, 1.0D), (double)this.getViewDistance());

    }
    @Override
    public void render(Door_Of_Seal_BlockEntity entity, float delta, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
        poseStack.pushPose();
        Direction dir = entity.getBlockState().getValue(Door_of_Seal_Block.FACING);
        if(dir == Direction.NORTH){
            poseStack.translate(0.5, 1.501F, 0.5F);
        }else if(dir == Direction.EAST){
            poseStack.translate(0.5F, 1.501F, 0.5F);
        }else if(dir == Direction.SOUTH){
            poseStack.translate(0.5, 1.501F, 0.5F);
        }else if(dir == Direction.WEST){
            poseStack.translate(0.5F, 1.501F, 0.5F);
        }
        poseStack.mulPose(dir.getOpposite().getRotation());
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        MODEL.animate(entity, delta);
        MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, overlay, 1, 1F, 1, 1);
        poseStack.popPose();
    }
}
