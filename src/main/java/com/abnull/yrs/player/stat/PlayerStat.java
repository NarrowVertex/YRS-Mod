package com.abnull.yrs.player.stat;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerStat {
	
	public int str = 0;
	public int def = 0;
	public int acc = 0;
	public int agi = 0;
	
	public PlayerStat()
	{
		
	}
	
	public PlayerStat(int str, int def, int acc, int agi)
	{
		this.str = str;
		this.def = def;
		this.acc = acc;
		this.agi = agi;
	}
	
	public NBTTagCompound write_stat_to_nbt()
	{
		NBTTagCompound stat_nbt = new NBTTagCompound();
		stat_nbt.setString("str", "" + str);
		stat_nbt.setString("def", "" + def);
		stat_nbt.setString("acc", "" + acc);
		stat_nbt.setString("agi", "" + agi);
		stat_nbt.setString("label_array", "str,def,acc,agi,");
		return stat_nbt;
	}
	
	public void read_stat_from_nbt(NBTTagCompound nbt)
	{
		str = Integer.parseInt(nbt.getString("str"));
		def = Integer.parseInt(nbt.getString("def"));
		acc = Integer.parseInt(nbt.getString("acc"));
		agi = Integer.parseInt(nbt.getString("agi"));
	}
}
