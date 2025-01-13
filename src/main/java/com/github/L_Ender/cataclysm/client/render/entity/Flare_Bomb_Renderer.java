package com.github.L_Ender.cataclysm.client.render.entity;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.model.entity.Flare_Bomb_Model;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.github.L_Ender.cataclysm.entity.projectile.Flare_Bomb_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class Flare_Bomb_Renderer extends EntityRenderer<Flare_Bomb_Entity> {

    private static final ResourceLocation OUTER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/flare_bomb_outer.png");

    private static final ResourceLocation INNER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/flare_bomb_inner.png");

    private final Flare_Bomb_Model model;

    public Flare_Bomb_Renderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        model = new Flare_Bomb_Model(renderManagerIn.bakeLayer(CMModelLayers.FLARE_BOMB_MODEL));
    }


    @Override
    public void render(Flare_Bomb_Entity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose((new Quaternion(new Vector3f(0, -1.0F, 0), entityYaw, true)));
        VertexConsumer VertexConsumer = bufferIn.getBuffer(CMRenderTypes.CMEyes(this.getTextureLocation(entityIn)));
        model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, 0, 0);
        model.renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        VertexConsumer VertexConsumer2 = bufferIn.getBuffer(CMRenderTypes.CMEyes(OUTER_TEXTURES));
        model.renderToBuffer(matrixStackIn, VertexConsumer2, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.4F);
        matrixStackIn.popPose();
    }

    protected int getBlockLightLevel(Flare_Bomb_Entity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Flare_Bomb_Entity entity) {
        return INNER_TEXTURES;
    }
}
