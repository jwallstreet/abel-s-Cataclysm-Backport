package com.github.L_Ender.cataclysm.client.render.entity;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.entity.The_Prowler_Model;
import com.github.L_Ender.cataclysm.client.render.layer.The_Prowler_Layer;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.The_Prowler_Entity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class The_Prowler_Renderer extends MobRenderer<The_Prowler_Entity, The_Prowler_Model> {
    private final RandomSource rnd = RandomSource.create();
    private static final ResourceLocation PROWLER_TEXTURES =new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler.png");
    private static final ResourceLocation PROWLER_LAYER_TEXTURES =new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler_layer.png");

    public The_Prowler_Renderer(Context renderManagerIn) {
        super(renderManagerIn, new The_Prowler_Model(), 0.7F);
       // this.addLayer(new LayerGenericGlowing(this, PROWLER_LAYER_TEXTURES));
        this.addLayer(new The_Prowler_Layer(this));

    }
    
    @Override
    protected float getFlipDegrees(The_Prowler_Entity entity) {
        return 0;
    }
    
    @Override
    public ResourceLocation getTextureLocation(The_Prowler_Entity entity) {
        return PROWLER_TEXTURES;
    }

    public Vec3 getRenderOffset(The_Prowler_Entity entityIn, float partialTicks) {
    	if (entityIn.getAttackState() == 1) {
            double d0 = 0.05D;
            return new Vec3(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    @Override
    protected void scale(The_Prowler_Entity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }
}