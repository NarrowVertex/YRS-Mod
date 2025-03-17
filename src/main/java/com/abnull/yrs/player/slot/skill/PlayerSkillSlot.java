package com.abnull.yrs.player.slot.skill;

import net.minecraft.nbt.NBTTagCompound;

import com.abnull.yrs.player.slot.PlayerSlotBase;

public class PlayerSkillSlot extends PlayerSlotBase {

	private String skill_name;
	
	public PlayerSkillSlot()
	{
		super(null);
		this.skill_name = "n";
	}
	
	public PlayerSkillSlot(NBTTagCompound data)
	{
		super(data);
		this.skill_name = data.getString("name");
	}
	
	@Override
	public void set_data(NBTTagCompound data)
	{
		super.set_data(data);
		this.skill_name = data.getString("name");
	}
	
	public String get_skill_name()
	{
		return this.skill_name;
	}
}
