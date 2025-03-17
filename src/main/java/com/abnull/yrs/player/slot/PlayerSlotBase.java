package com.abnull.yrs.player.slot;

import net.minecraft.nbt.NBTTagCompound;

public abstract class PlayerSlotBase {
	
	private NBTTagCompound slot_data;
	
	public PlayerSlotBase(NBTTagCompound data)
	{
		this.slot_data = data;
	}
	
	public void set_data(NBTTagCompound data)
	{
		this.slot_data = data;
	}
	
	public NBTTagCompound get_data()
	{
		return this.slot_data;
	}
}