package com.github.L_Ender.cataclysm.inventory;

import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TeddyBearMenu extends AbstractContainerMenu {
    private final Container bearContainer;
    private final Teddy_Bear_Entity teddyBear;

    public TeddyBearMenu(int containerId, Inventory playerInventory, Container bearInventory, final Teddy_Bear_Entity bear) {
        super((MenuType)null, containerId);
        this.bearContainer = bearInventory;
        this.teddyBear = bear;
        bearInventory.startOpen(playerInventory.player);

        // Add teddy bear inventory slots (3x3 grid)
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 3; ++col) {
                this.addSlot(new TeddyBearSlot(bearInventory, col + row * 3, 62 + col * 18, 17 + row * 18));
            }
        }

        // Add player inventory slots
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add player hotbar slots
        for(int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public boolean stillValid(Player player) {
        return !this.teddyBear.hasInventoryChanged(this.bearContainer) && 
               this.bearContainer.stillValid(player) && 
               this.teddyBear.isAlive() && 
               this.teddyBear.distanceTo(player) < 8.0F;
    }

    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex < this.bearContainer.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.bearContainer.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.bearContainer.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.bearContainer.stopOpen(player);
        if (teddyBear != null) {
            teddyBear.setAttackState(5); // Set to chest_close animation state
        }
    }
}