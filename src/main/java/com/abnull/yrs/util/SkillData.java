package com.abnull.yrs.util;

import java.util.ArrayList;
import java.util.List;

import com.abnull.yrs.proxy.ClientProxy;

import net.minecraft.nbt.NBTTagCompound;

public class SkillData {

	public NBTTagCompound skill_data;
	
	public String skill_name;
	public float cooltime;
	public String description;
	public int current_level;
	public int max_skill_level;
	public int cost_mana;
	public List<String> skill_lore_array = new ArrayList<String>();
	public List<String> level_lore_array = new ArrayList<String>();
	
	public NBTTagCompound need_stat_data;
	public NBTTagCompound need_skill_data;
	public int need_player_level;
	
	public boolean is_need_stat = false;
	public boolean is_need_skill = false;
	
	
	public SkillData(NBTTagCompound data, int level)
	{
		skill_data = data;
		skill_name = data.getString("name");
		cooltime = Float.parseFloat(data.getString("cooltime"));
		description = data.getString("description");
		current_level = level;
		max_skill_level = Integer.parseInt(data.getString("max-skill-level"));
		cost_mana = Integer.parseInt(data.getString("cost-mana"));
		NBTTagCompound lore_data = data.getCompoundTag("lore");
		NBTTagCompound level_lore_data = data.getCompoundTag("level-lore");
		for(int n = 0; n < lore_data.getInteger("tag_contents_count"); n++) 
		{
			skill_lore_array.add(lore_data.getString("" + n));
		}
		for(int n = 0; n < level_lore_data.getInteger("tag_contents_count"); n++) 
		{
			level_lore_array.add(level_lore_data.getString("" + n));
		}
		
		if(data.hasKey("need-stat"))
		{
			need_stat_data = data.getCompoundTag("need-stat");
			is_need_stat = true;
		}
		if(data.hasKey("need-skill"))
		{
			need_skill_data = data.getCompoundTag("need-skill");
			is_need_skill = true;
		}
		
		if(data.hasKey("need-level"))
			need_player_level = Integer.parseInt(data.getString("need-level"));
		else
			need_player_level = 0;
	}
	
	public String get_skill_permission()
	{
		NBTTagCompound player_data = ClientProxy.player_data;
		
		int skill_level = Integer.parseInt(player_data.getCompoundTag("skill_level_data").getString(skill_name));
		if(skill_level <= 0)
		{
			return "need_activation";
		}
		
		// Stat
		if(is_need_stat)
		{
			NBTTagCompound player_stat_data = player_data.getCompoundTag("stat");
			for(int n = 0; n < need_stat_data.getInteger("tag_contents_count"); n++)
			{
				String contents = need_stat_data.getString("" + n);
				String need_stat_name = contents.split(":")[0];
				int need_stat_value = Integer.parseInt(contents.split(":")[1]);
				int stat_value = Integer.parseInt(player_stat_data.getString(need_stat_name));
				
				if(stat_value < need_stat_value)
				{
					return "need_stat";
				}
			}
		}
		
		// Skill
		if(is_need_skill)
		{
			String player_skill_name_array = player_data.getString("skill_array");
			for(int n = 0; n < need_skill_data.getInteger("tag_contents_count"); n++)
			{
				String need_skill_name = need_skill_data.getString("" + n);
				if(!player_skill_name_array.contains(need_skill_name))
				{
					return "need_skill";
				}
			}
		}
		
		// Player Level
		if(need_player_level != 0)
		{
			int player_level = Integer.parseInt(player_data.getString("exp-level"));
			if(player_level < need_player_level)
			{
				return "need_player_level";
			}
		}
		
		return "none";
	}

	public boolean has_skill_permission()
	{
		if(!get_skill_permission().equals("none"))
			return false;
		return true;
	}

	public boolean has_skill_upgrading_permission()
	{
		NBTTagCompound player_data = ClientProxy.player_data;
		
		if(!has_skill_permission())
			if(!get_skill_permission().equals("need_activation"))
			{
				return false;
			}
		
		int skill_point = Integer.parseInt(player_data.getString("skill-point"));
		if(skill_point <= 0)
		{
			return false;
		}
		
		int skill_master_level = max_skill_level;
		int skill_level = Integer.parseInt(player_data.getCompoundTag("skill_level_data").getString(skill_name));
		if(skill_level >= skill_master_level)
		{
			return false;
		}
		
		return true;
	}

	public boolean is_cooltiming()
	{
		long current_time = System.currentTimeMillis();
		long last_skill_used_time = ClientProxy.skill_cooltime_map.get(skill_name);
		if((current_time - last_skill_used_time) < cooltime * 1000)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
