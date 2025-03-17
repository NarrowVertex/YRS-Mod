package com.abnull.yrs.player.util;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerBasicDataManager {

	public static void set_max_health(EntityPlayer player, float value)
	{
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(value);
	}
	
	public static void set_health(EntityPlayer player, float value)
	{
		player.setHealth(value);
	}
	
	public static void set_max_mana(NBTTagCompound data, int value)
	{
		data.setString("max-mp", "" + value);
		set_mana_balance(data);
	}
	
	public static void set_mana(NBTTagCompound data, int value)
	{
		data.setString("mp", "" + value);
		set_mana_balance(data);
	}
	
	public static void set_mana_balance(NBTTagCompound data)
	{
		int max_mp = Integer.parseInt(data.getString("max-mp"));
		int mp = Integer.parseInt(data.getString("mp"));
		
		data.setString("mp", "" + Math.min(max_mp, mp));
	}
}
