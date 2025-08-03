package com.github.L_Ender.cataclysm.client.render.entity;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.model.entity.Teddy_Bear_Model;
import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Teddy_Bear_Renderer extends MobRenderer<Teddy_Bear_Entity, Teddy_Bear_Model> {

    private static final ResourceLocation TEDDY_BEAR_TEXTURES = new ResourceLocation(Cataclysm.MODID, "textures/entity/teddy_bear.png");

    public Teddy_Bear_Renderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new Teddy_Bear_Model(renderManagerIn.bakeLayer(CMModelLayers.TEDDY_BEAR_MODEL)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Teddy_Bear_Entity entity) {
        return TEDDY_BEAR_TEXTURES;
    }

    @Override
    protected void scale(Teddy_Bear_Entity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }
}