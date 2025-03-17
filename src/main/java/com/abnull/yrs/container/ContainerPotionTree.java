package com.abnull.yrs.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.abnull.yrs.tileentity.TileEntityPotionTree;

public class ContainerPotionTree extends Container{
	
	private TileEntityPotionTree tile_potion_tree;
	
	public ContainerPotionTree(InventoryPlayer p1, TileEntityPotionTree p2)
	{
		this.tile_potion_tree = p2;
		for(int n = 0; n < this.tile_potion_tree.getSizeInventory(); n++)
		{
			this.addSlotToContainer(new Slot(p2, n, 62 + n * 18, 20));
		}
		
		int i;
		for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(p1, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(p1, i, 8 + i * 18, 109));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ < tile_potion_tree.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, tile_potion_tree.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, tile_potion_tree.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
