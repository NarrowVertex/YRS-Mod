package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import com.abnull.yrs.container.ContainerPotionTree;
import com.abnull.yrs.tileentity.TileEntityPotionTree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiPotionTree extends CustomGuiContainerBase {

    private static final ResourceLocation potion_tree_gui = new ResourceLocation("yrs", "textures/gui/potion_tree_gui.png");
    
	private TileEntityPotionTree tile_potion_tree;

	public CustomGuiPotionTree(InventoryPlayer p1, TileEntityPotionTree p2)
	{
		super(new ContainerPotionTree(p1, p2));
		this.tile_potion_tree = p2;
		this.ySize = 133;
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int mode)
	{
		super.mouseMovedOrUp(x, y, mode);
		if (this.getSlotAtPosition(x, y) != null && mode == 0 && this.inventorySlots.func_94530_a((ItemStack)null, this.getSlotAtPosition(x, y)))
        {
			if (isShiftKeyDown())
	        {
				NBTTagCompound potion_slots_data = new NBTTagCompound();
				ItemStack[] item_stack_array = tile_potion_tree.item_stack_array;
				for(int n = 0; n < item_stack_array.length; n++)
				{
					ItemStack item_stack = item_stack_array[n];
					NBTTagCompound potion_slot_data = new NBTTagCompound();
					if(item_stack != null)
					{
						NBTTagCompound potion_data = new NBTTagCompound();
						item_stack.writeToNBT(potion_data);
						potion_slot_data.setTag("data", potion_data);
						potion_slot_data.setBoolean("is_null", false);
					}
					else
					{
						potion_slot_data.setBoolean("is_null", true);
					}
					potion_slots_data.setTag("" + n, potion_slot_data);
				}
				com.abnull.yrs.proxy.ClientProxy.client_sending_data_manager.send_data_to_server("update_potion_slot", potion_slots_data);
	        }
        }
	}
	
	public void onGuiClosed()
    {
		super.onGuiClosed();
		
		NBTTagCompound potion_slots_data = new NBTTagCompound();
		ItemStack[] item_stack_array = tile_potion_tree.item_stack_array;
		for(int n = 0; n < item_stack_array.length; n++)
		{
			ItemStack item_stack = item_stack_array[n];
			NBTTagCompound potion_slot_data = new NBTTagCompound();
			if(item_stack != null)
			{
				NBTTagCompound potion_data = new NBTTagCompound();
				potion_data = item_stack.writeToNBT(potion_data);
				potion_slot_data.setTag("data", potion_data);
				potion_slot_data.setBoolean("is_null", false);
			}
			else
			{
				potion_slot_data.setBoolean("is_null", true);
			}
			potion_slots_data.setTag("" + n, potion_slot_data);
		}
		com.abnull.yrs.proxy.ClientProxy.client_sending_data_manager.send_data_to_server("update_potion_slot", potion_slots_data);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		int gui_x = (this.width / 2) - (176 / 2);
        int gui_y = (this.height / 2) - (133 / 2) - 1;
        
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(potion_tree_gui);
        this.drawTexturedModalRect(gui_x, gui_y, 0, 0, 176, 133);
        this.drawString(Minecraft.getMinecraft().fontRenderer, "Potion Slot", gui_x + 3, gui_y + 3, 0xFFFFFF);
	}
	
	private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);

            if (this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, p_146975_1_, p_146975_2_))
            {
                return slot;
            }
        }
        return null;
    }
}
