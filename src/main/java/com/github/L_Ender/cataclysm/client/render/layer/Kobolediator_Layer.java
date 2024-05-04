package com.github.L_Ender.cataclysm.client.render.layer;

import com.github.L_Ender.cataclysm.client.model.entity.ModelKobolediator;
import com.github.L_Ender.cataclysm.client.render.entity.RendererKobolediator;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.Kobolediator_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Kobolediator_Layer extends RenderLayer<Kobolediator_Entity, ModelKobolediator> {
    private static final ResourceLocation LAYER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/koboleton/kobolediator_layer.png");

    public Kobolediator_Layer(RendererKobolediator renderIn) {
        super(renderIn);

    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Kobolediator_Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getAttackState() != 1 && entity.isAlive()) {
            RenderType eyes = RenderType.eyes(LAYER_TEXTURES);
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}


