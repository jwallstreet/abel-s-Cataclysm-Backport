package com.github.L_Ender.cataclysm.client.render.entity;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.entity.ModelAxe_Blade;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.github.L_Ender.cataclysm.entity.projectile.Axe_Blade_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererAxe_Blade extends EntityRenderer<Axe_Blade_Entity>
{
    private static final ResourceLocation[] TEXTURE_PROGRESS = new ResourceLocation[5];

    public ModelAxe_Blade model;

    public RendererAxe_Blade(EntityRendererProvider.Context manager)
    {
        super(manager);
        this.model = new ModelAxe_Blade();
        for(int i = 0; i < 5; i++){
            TEXTURE_PROGRESS[i] = new ResourceLocation(Cataclysm.MODID,"textures/entity/draugar/axe_blade_" + i + ".png");
        }
    }

    @Override
    protected int getBlockLightLevel(Axe_Blade_Entity entity, BlockPos pos)
    {
        return 15;
    }

    @Override
    public void render(Axe_Blade_Entity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0, -2.25F, 0);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityIn.getYRot()));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
        VertexConsumer vertexconsumer = bufferIn.getBuffer(CMRenderTypes.getGhost(this.getTextureLocation(entityIn)));
        model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, 0, 0);
        float hide = ((float) entityIn.getTransparency() / 80);
        float alpha = (1F - hide) ;
        this.model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, Mth.clamp(alpha, 0, 1));
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(Axe_Blade_Entity entity)
    {
        return getGrowingTexture(entity,(int) ((entity.tickCount * 0.5F) % 4));
    }

    public ResourceLocation getGrowingTexture(Axe_Blade_Entity entity, int age) {
        return  TEXTURE_PROGRESS[Mth.clamp(age, 0, 4)];
    }

}
