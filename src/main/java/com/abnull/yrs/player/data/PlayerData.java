package com.abnull.yrs.player.data;

import java.util.HashMap;

import com.abnull.yrs.player.slot.skill.PlayerSkillSlot;
import com.abnull.yrs.player.stat.PlayerStat;
import com.abnull.yrs.player.util.PlayerBasicDataManager;
import com.abnull.yrs.proxy.ServerProxy;
import com.abnull.yrs.region.Region;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {

	public EntityPlayer player;
	public int player_id;
	public String player_name;
	public NBTTagCompound data;
	public PlayerStat player_stat;
	public PlayerSkillSlot[] player_skill_slot;
	public ItemStack[] player_potion_slot;
	
	public NBTTagCompound last_job_data;
	
	public long pre_hp_regen_time;
	public long pre_mp_regen_time;
	
	public HashMap<String, Long> skill_cooltime_map = new HashMap<String, Long>();
	
	public Region player_current_region = new Region("None", null);
	public Region player_last_region = new Region("None", null);
	
	public PlayerData(EntityPlayer player)
	{
		this.player = player;
		player_id = player.getEntityId();
		player_name = player.getCommandSenderName();
		data = new NBTTagCompound();
		player_stat = new PlayerStat();
		player_skill_slot = new PlayerSkillSlot[6];
		player_potion_slot = new ItemStack[3];
		// skill_cooltime_map;
	}
	
	public long get_skill_cooltime(String skill_name)
	{
		if(skill_cooltime_map.containsKey(skill_name))
		{
			return skill_cooltime_map.get(skill_name);
		}
		else
		{
			skill_cooltime_map.put(skill_name, 0L);
			return 0L;
		}
	}
	
	public void set_skill_cooltime(String skill_name)
	{
		if(skill_cooltime_map.containsKey(skill_name))
		{
			skill_cooltime_map.remove(skill_name);
			skill_cooltime_map.put(skill_name, System.currentTimeMillis());
			// skill_cooltime_map.replace(skill_name, System.currentTimeMillis());
			// skill_cooltime_map.replace(skill_name, System.currentTimeMillis());
		}
		else
		{
			skill_cooltime_map.put(skill_name, System.currentTimeMillis());
		}
	}
}
