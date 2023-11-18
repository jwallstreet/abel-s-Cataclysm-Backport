package com.github.L_Ender.cataclysm.client.render.item.Curios;

import com.github.L_Ender.cataclysm.client.model.armor.CMModelLayers;
import com.github.L_Ender.cataclysm.client.model.item.CuriosModel.Sandstorm_In_A_BottleModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class PendantRenderer implements ICurioRenderer {
    private final Sandstorm_In_A_BottleModel model;
    private static final ResourceLocation TEXTURE = new ResourceLocation("cataclysm:textures/curios/abyss_laser_beam.png");

    public PendantRenderer() {
        this.model = new Sandstorm_In_A_BottleModel(Minecraft.getInstance().getEntityModels().bakeLayer(CMModelLayers.SANDSTORM_IN_A_BOTTLE_MODEL));
    }


    public ResourceLocation getCuriosTexture() {
        return TEXTURE;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ICurioRenderer.followBodyRotations(slotContext.entity(), this.model);
        VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(getCuriosTexture()), false, stack.hasFoil());
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
