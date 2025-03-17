package com.abnull.yrs.file;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class FilePlayerBase extends FileBase{

	public String player_name;
	public EntityPlayer player;
	
	public FilePlayerBase(String path) {
		super("", path);
	}
	
	public void set(String player_name, EntityPlayer player)
	{
		this.player_name = player_name;
		this.player = player;
		file = new File(this.base_path + "/" + this.file_path + "/" + player.getCommandSenderName() + ".txt");
	}
}
