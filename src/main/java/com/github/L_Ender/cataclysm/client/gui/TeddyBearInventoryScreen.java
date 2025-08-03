package com.github.L_Ender.cataclysm.client.gui;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;
import com.github.L_Ender.cataclysm.inventory.TeddyBearMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TeddyBearInventoryScreen extends AbstractContainerScreen<TeddyBearMenu> {
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(Cataclysm.MODID, "textures/gui/teddy_bear_inventory.png");
    private final Teddy_Bear_Entity teddyBear;
    private float xMouse;
    private float yMouse;

    public TeddyBearInventoryScreen(TeddyBearMenu menu, Inventory playerInventory, Teddy_Bear_Entity bear) {
        super(menu, playerInventory, bear.getDisplayName());
        this.teddyBear = bear;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        RenderSystem.setShaderTexture(0, INVENTORY_TEXTURE);
        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
        
        // Render the teddy bear entity in the GUI
        InventoryScreen.renderEntityInInventory(x + 35, y + 60, 25, (float)(x + 35) - this.xMouse, (float)(y + 35) - this.yMouse, teddyBear);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}