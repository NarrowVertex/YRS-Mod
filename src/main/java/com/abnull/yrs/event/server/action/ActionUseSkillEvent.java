package com.abnull.yrs.event.server.action;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ActionUseSkillEvent extends ActionEvent {

	public ActionUseSkillEvent(EntityPlayer player, String p_name, String a_name, NBTTagCompound data)
	{
		super(player, p_name, a_name, data);
	}

}
