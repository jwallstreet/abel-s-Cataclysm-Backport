package com.github.L_Ender.cataclysm.client.render.entity;


import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.entity.ModelCoral_Golem;
import com.github.L_Ender.cataclysm.entity.Deepling.Coral_Golem_Entity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererCoral_Golem extends MobRenderer<Coral_Golem_Entity, ModelCoral_Golem> {

    private static final ResourceLocation CORALSSUS_TEXTURES =new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/coral_golem.png");

    public RendererCoral_Golem(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelCoral_Golem(), 1.5F);

    }
    @Override
    public ResourceLocation getTextureLocation(Coral_Golem_Entity entity) {
        return CORALSSUS_TEXTURES;
    }

    @Override
    protected void scale(Coral_Golem_Entity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.75F, 1.75F, 1.75F);
    }
}

