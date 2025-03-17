package com.abnull.yrs.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPotionTree extends TileEntity implements IInventory{
	
	public EntityPlayer player;
	public ItemStack[] item_stack_array = new ItemStack[3];
    private String customName;
	
	public TileEntityPotionTree(EntityPlayer player)
	{
		this.player = player;
		
		NBTTagCompound data = new NBTTagCompound();
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
	    {
			data = com.abnull.yrs.proxy.ClientProxy.player_data.getCompoundTag("potion_tree_data");
	    }
		else
		{
			data = com.abnull.yrs.proxy.ServerProxy.player_data_manager.get_player_data(player).data.getCompoundTag("potion_tree_data");
		}
		
		for(int n = 0; n < item_stack_array.length; n++)
		{
			NBTTagCompound potion_data = data.getCompoundTag("" + n);
			boolean is_null = potion_data.getBoolean("is_null");
			
			ItemStack item_stack = new ItemStack(new Item());
			if(!is_null)
			{
    			item_stack.readFromNBT(potion_data.getCompoundTag("data"));
			}
			else
			{
				item_stack = null;
			}
			this.item_stack_array[n] = item_stack;
		}
		
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
	    {
			com.abnull.yrs.proxy.ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, com.abnull.yrs.proxy.ServerProxy.player_data_manager.get_player_data(player).data, "potion_slot");
	    }
	}
	
	@Override
	public int getSizeInventory() {
		return this.item_stack_array.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		if (p_70301_1_ < 0 || p_70301_1_ >= this.getSizeInventory())
	        return null;
		
	    return this.item_stack_array[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.getStackInSlot(index) != null) {
	        ItemStack itemstack;
	        
	        if (this.getStackInSlot(index).stackSize <= count) {
	            itemstack = this.getStackInSlot(index);
	            this.setInventorySlotContents(index, null);
	            this.markDirty();
	            return itemstack;
	        } else {
	            itemstack = this.getStackInSlot(index).splitStack(count);
	            
	            if (this.getStackInSlot(index).stackSize <= 0) {
	                this.setInventorySlotContents(index, null);
	            } else {
	                //Just to show that changes happened
	                this.setInventorySlotContents(index, this.getStackInSlot(index));
	            }
	            
	            this.markDirty();
	            return itemstack;
	        }
	    } else {
	        return null;
	    }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = this.getStackInSlot(index);
	    this.setInventorySlotContents(index, null);
	    return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory())
	        return;

	    if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        stack.stackSize = this.getInventoryStackLimit();
	        
	    if (stack != null && stack.stackSize == 0)
	        stack = null;
	    
	    this.item_stack_array[index] = stack;
	    
	    if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
	    {
	    	NBTTagCompound potion_data = new NBTTagCompound();
	    	if(stack != null)
	    	{
	    		NBTTagCompound data = new NBTTagCompound();
	    		data = stack.writeToNBT(data);
	    		potion_data.setTag("data", data);
	    		potion_data.setBoolean("is_null", false);
	    	}
	    	else
	    	{
	    		potion_data.setBoolean("is_null", true);
	    	}
	    	com.abnull.yrs.proxy.ServerProxy.player_data_manager.get_player_data(player).data.getCompoundTag("potion_tree_data").setTag("" + index, potion_data);
	    	com.abnull.yrs.proxy.ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, com.abnull.yrs.proxy.ServerProxy.player_data_manager.get_player_data(player).data, "potion_tree_data");
	    }
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "potion tree";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);

	    NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbt.setTag("Items", list);

	    if (this.hasCustomInventoryName()) {
	        nbt.setString("CustomName", this.getInventoryName());
	    }
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);

	    NBTTagList list = nbt.getTagList("Items", 10);
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }

	    if (nbt.hasKey("CustomName", 8)) {
	        this.customName = nbt.getString("CustomName");
	    }
	}
	
	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}
	
	public void updateEntity()
    {
		
    }

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

}
