package com.github.L_Ender.cataclysm.client.model;

import com.github.L_Ender.cataclysm.client.model.armor.Bloom_Stone_Pauldrons_Model;
import com.github.L_Ender.cataclysm.client.model.armor.Bone_Reptile_Armor_Model;
import com.github.L_Ender.cataclysm.client.model.armor.Cursium_Armor_Model;
import com.github.L_Ender.cataclysm.client.model.armor.Ignitium_Armor_Model;
import com.github.L_Ender.cataclysm.client.model.armor.MonstrousHelm_Model;
import com.github.L_Ender.cataclysm.client.model.armor.ignitium_Elytra_chestplate_Model;
import com.github.L_Ender.cataclysm.client.model.block.AptrgangrHeadModel;
import com.github.L_Ender.cataclysm.client.model.block.DraugrHeadModel;
import com.github.L_Ender.cataclysm.client.model.block.KobolediatorHeadModel;
import com.github.L_Ender.cataclysm.client.model.entity.Ignited_Berserker_Model;
import com.github.L_Ender.cataclysm.client.model.item.CuriosModel.Sandstorm_In_A_BottleModel;
import com.github.L_Ender.cataclysm.client.model.item.CuriosModel.Sticky_Gloves_Model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class CMModelLayers {

    public static final ModelLayerLocation ELYTRA_ARMOR = createLocation("elytra_armor", "main");
    public static final ModelLayerLocation MONSTROUS_HELM = createLocation("monstrous", "main");
    public static final ModelLayerLocation IGNITIUM_ARMOR_MODEL = createLocation("ignitium_armor_model", "main");
    public static final ModelLayerLocation IGNITIUM_ARMOR_MODEL_LEGS = createLocation("ignitium_armor_model_leg", "main");
    public static final ModelLayerLocation BLOOM_STONE_PAULDRONS_MODEL = createLocation("bloom_stone_pauldrons_model", "main");
    public static final ModelLayerLocation BONE_REPTILE_ARMOR_MODEL = createLocation("bone_reptile_armor_model", "main");
    public static final ModelLayerLocation SANDSTORM_IN_A_BOTTLE_MODEL = createLocation("sandstorm_in_a_bottle_model", "main");
    public static final ModelLayerLocation STICKY_GLOVES_MODEL = createLocation("sticky_gloves_model", "main");
    public static final ModelLayerLocation KOBOLEDIATOR_HEAD_MODEL = createLocation("kobolediator_head_model", "main");
    public static final ModelLayerLocation APTRGANGR_HEAD_MODEL = createLocation("aptrgangr_head_model", "main");
    public static final ModelLayerLocation DRAUGR_HEAD_MODEL = createLocation("draugr_head_model", "main");
    public static final ModelLayerLocation IGNITED_BERSERKER_MODEL = createLocation("ignited_berserker_model", "main");
    public static final ModelLayerLocation CURSIUM_ARMOR_MODEL = createLocation("cursium_armor_model", "main");
    public static final ModelLayerLocation CURSIUM_ARMOR_MODEL_LEGS = createLocation("cursium_armor_model_leg", "main");

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MONSTROUS_HELM, () -> MonstrousHelm_Model.createArmorLayer(new CubeDeformation(0.3F)));
        event.registerLayerDefinition(IGNITIUM_ARMOR_MODEL, () -> Ignitium_Armor_Model.createArmorLayer(new CubeDeformation(0.6F)));
        event.registerLayerDefinition(BLOOM_STONE_PAULDRONS_MODEL, () -> Bloom_Stone_Pauldrons_Model.createArmorLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(ELYTRA_ARMOR, () -> ignitium_Elytra_chestplate_Model.createArmorLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(IGNITIUM_ARMOR_MODEL_LEGS, () -> Ignitium_Armor_Model.createArmorLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(SANDSTORM_IN_A_BOTTLE_MODEL, () -> Sandstorm_In_A_BottleModel.createLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(BONE_REPTILE_ARMOR_MODEL, () -> Bone_Reptile_Armor_Model.createArmorLayer(new CubeDeformation(1.0F)));
        event.registerLayerDefinition(STICKY_GLOVES_MODEL, () -> Sticky_Gloves_Model.createLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(KOBOLEDIATOR_HEAD_MODEL, KobolediatorHeadModel::createHeadLayer);
        event.registerLayerDefinition(APTRGANGR_HEAD_MODEL, AptrgangrHeadModel::createHeadLayer);
        event.registerLayerDefinition(DRAUGR_HEAD_MODEL, DraugrHeadModel::createHeadLayer);
        event.registerLayerDefinition(IGNITED_BERSERKER_MODEL, Ignited_Berserker_Model::createBodyLayer);
        event.registerLayerDefinition(CURSIUM_ARMOR_MODEL, () -> Cursium_Armor_Model.createArmorLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(CURSIUM_ARMOR_MODEL_LEGS, () -> Cursium_Armor_Model.createArmorLayer(new CubeDeformation(0.2F)));
    }

    private static ModelLayerLocation createLocation(String model, String layer) {
        return new ModelLayerLocation(new ResourceLocation("cataclysm", model), layer);
    }
}
