package com.abnull.yrs.file;

import net.minecraft.nbt.NBTTagCompound;

public interface IFile {
	
	public void init();

	public void save(NBTTagCompound data);
	
	public void load(NBTTagCompound data);
	
}
