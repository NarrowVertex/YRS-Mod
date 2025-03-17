package com.abnull.yrs.file;

import java.util.HashMap;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.proxy.ServerProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class FileManager {

	public HashMap<String, FileBase> file_map = new HashMap<String, FileBase>();
	public HashMap<String, FilePlayerBase> file_player_map = new HashMap<String, FilePlayerBase>();
	
	public FileManager()
	{
		file_map.put("config", new FileConfig());
		file_map.put("stat_config", new FileStatConfig());
		file_map.put("job", new FileJob());
		file_map.put("skill", new FileSkill());
		file_map.put("skill_texture", new FileSkillTexture());
		file_map.put("background_sound", new FileBackgroundSound());
		file_map.put("region", new FileRegion());
		
		file_player_map.put("player_data", new FilePlayerData());
	  	file_player_map.put("player_stat", new FilePlayerStat());
		file_player_map.put("player_skill_level", new FilePlayerSkillLevel());
		file_player_map.put("player_skill_slot", new FilePlayerSkillSlot());
		file_player_map.put("player_potion_tree", new FilePlayerPotionTree());
	}
	
	public FileBase get_file(String file_name)
	{
		return file_map.containsKey(file_name) ? file_map.get(file_name) : null;
	}
	
	public boolean load_file(NBTTagCompound data, String file_name)
	{
		FileBase file = file_map.get(file_name);
		if(file == null)
		{
			YRSMod.debug.print("'" + file_name + "' isn't loaded- - -");
			return false;
		}
		else
		{
			file.load(data);
			YRSMod.debug.print("'" + file_name + "' is loaded- - -");
			return true;
		}
	}
	
	public boolean save_file(NBTTagCompound data, String file_name)
	{
		FileBase file = file_map.get(file_name);
		if(file == null)
		{
			return false;
		}
		else
		{
			file.save(data);
			return true;
		}
	}
	
	public void load_player_file(NBTTagCompound data, String file_name, String player_name, EntityPlayer player)
	{
		FilePlayerBase file_player_base = file_player_map.get(file_name);
		file_player_base.set(player_name, player);
		file_player_base.load(data);
	}
	
	public void save_player_file(NBTTagCompound data, String file_name, String player_name, EntityPlayer player)
	{
		FilePlayerBase file_player_base = file_player_map.get(file_name);
		file_player_base.set(player_name, player);
		file_player_base.save(data);
	}
}
