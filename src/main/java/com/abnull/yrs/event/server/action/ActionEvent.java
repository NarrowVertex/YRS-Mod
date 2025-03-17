package com.abnull.yrs.event.server.action;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event;

public class ActionEvent extends Event{

	public EntityPlayer player;
	public String player_name;
	
	public String action_name;
	public NBTTagCompound action_data;
	
	public ActionEvent(EntityPlayer player, String p_name, String a_name, NBTTagCompound data)
	{
		this.player = player;
		player_name = p_name;
		
		action_name = a_name;
		action_data = data;
	}
}
