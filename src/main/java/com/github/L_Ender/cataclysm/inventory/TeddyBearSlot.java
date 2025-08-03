package com.github.L_Ender.cataclysm.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TeddyBearSlot extends Slot {
    public TeddyBearSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Allow all items for now, can add restrictions later
        return true;
    }
}