package com.abnull.yrs.player.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.player.slot.skill.PlayerSkillSlot;
import com.abnull.yrs.player.stat.PlayerStat;
import com.abnull.yrs.player.util.PlayerBasicDataManager;
import com.abnull.yrs.proxy.ServerProxy;
import com.abnull.yrs.region.Region;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataManager {

	private HashMap<String, PlayerData> data_player_map = new HashMap<String, PlayerData>();
	
	public PlayerDataManager()
	{
		
	}
	
	public void add_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		PlayerData player_data = new PlayerData(player);
		data_player_map.put(player_name, player_data);
	}
	
	public boolean has_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		return data_player_map.containsKey(player_name);
	}
	
	public boolean has_player_data_by_name(String player_name)
	{
		return data_player_map.containsKey(player_name);
	}
	
	public PlayerData get_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		return data_player_map.get(player_name);
	}
	
	public PlayerData get_player_data_by_name(String player_name)
	{
		return data_player_map.get(player_name);
	}
	
	public void remove_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		data_player_map.remove(player_name);
	}
	
	public Set<String> get_player_name_list() 
	{
		return data_player_map.keySet();
	}
	
	public void load_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		PlayerData player_data = get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		PlayerStat player_stat = player_data.player_stat;
		PlayerSkillSlot[] player_skill_slot = player_data.player_skill_slot;
		
		NBTTagCompound job_data = ServerProxy.job_data;
		
		float max_health = Float.parseFloat(data.getString("max-hp"));
		float health = Float.parseFloat(data.getString("hp"));
		
		PlayerBasicDataManager.set_max_health(player, max_health);
		PlayerBasicDataManager.set_health(player, health);
		 
		String[] player_job_array = data.getString("jobs").split(";");
		player_data.last_job_data = job_data.getCompoundTag(player_job_array[player_job_array.length - 1]);
		
		String region_name = data.getString("region");
		if(region_name.equals(""))
		{
			player_data.player_current_region = new Region("None", null);
			player_data.data.setString("region", "None");
			if(!player_data.data.getString("label_array").contains("region"))
			{
				player_data.data.setString("label_array", player_data.data.getString("label_array") + ",region:None,");
			}
		}
		else if(!region_name.equals("None"))
		{
			player_data.player_current_region = ServerProxy.region_manager.get_region(region_name);
		}
		
		NBTTagCompound player_job_data = new NBTTagCompound();
		for(int n = 0; n < player_job_array.length; n++)
		{
			String job_name = player_job_array[n];
			player_job_data.setTag(job_name, job_data.getCompoundTag(job_name));
		}
		data.setTag("job_data", player_job_data);
		
		NBTTagCompound player_skill_level_data = new NBTTagCompound();
		ServerProxy.file_manager.load_player_file(player_skill_level_data, "player_skill_level", player_name, player);
		data.setTag("skill_level_data", player_skill_level_data);
		
		NBTTagCompound player_skill_slot_data = new NBTTagCompound();
		ServerProxy.file_manager.load_player_file(player_skill_slot_data, "player_skill_slot", player_name, player);
		for(int n = 0; n < 6; n++)
		{
			String skill_name = player_skill_slot_data.getString("slot" + (n + 1));
			if(skill_name.equals("n"))
			{
				player_skill_slot[n] = new PlayerSkillSlot();
			}
			else
			{
				player_skill_slot[n] = new PlayerSkillSlot(ServerProxy.skill_data.getCompoundTag(skill_name));
			}
		}
		data.setTag("skill_slot", player_skill_slot_data);
		
		NBTTagCompound player_stat_data = new NBTTagCompound();
		ServerProxy.file_manager.load_player_file(player_stat_data, "player_stat", player_name, player);
		player_stat.read_stat_from_nbt(player_stat_data);
		data.setTag("stat", player_stat_data);
		
		int player_id = player.getEntityId();
		int str_point = player_stat.str;
		int def_point = player_stat.def;
		int acc_point = player_stat.acc;
		int agi_point = player_stat.agi;
		ServerProxy.plugin_communication_manager.get_plugin_data("YRSModHelpPlugin").send_message("PSD", new String[]{ 
				"" + player_id,
				"" + str_point,
				"" + def_point,
				"" + acc_point,
				"" + agi_point });
		
		NBTTagCompound player_potion_data = new NBTTagCompound();
		ServerProxy.file_manager.load_player_file(player_potion_data, "player_potion_tree", player_name, player);
		for(int n = 0; n < 3; n++)
		{
			NBTTagCompound potion_data = player_potion_data.getCompoundTag("" + n);
			boolean is_null = potion_data.getBoolean("is_null");
			
			ItemStack item_stack = new ItemStack(new Item());
			if(!is_null)
			{
    			item_stack.readFromNBT(potion_data.getCompoundTag("data"));
			}
			else
			{
				item_stack = null;
			}
			player_data.player_potion_slot[n] = item_stack;
		}
		data.setTag("potion_tree_data", player_potion_data);
		
		String player_skill_array = "";
		for(int n = 0; n < player_job_array.length; n++)
		{
			String job_name = player_job_array[n];
			player_skill_array += job_data.getCompoundTag(job_name).getString("skill") + ",";
		}
		data.setString("skill_array", player_skill_array);
		ServerProxy.sending_data_manager.send_big_data_to_player(player, "skill_texture", ServerProxy.skill_texture_data, player_skill_array);
		
		NBTTagCompound background_sound_data = ServerProxy.background_sound_data;
		String sound_name_array = background_sound_data.getString("sound_name_array");
		YRSMod.debug.print_debug("PlayerDataManager", 178, "Sending background Sound Data Array [" + sound_name_array + "]");
		// ServerProxy.sending_data_manager.send_big_data_to_player(player, "background_sound", background_sound_data, sound_name_array);
		
		String[] sound_name_list = background_sound_data.getString("sound_name_list").split(",");
		for(int n = 0; n < sound_name_list.length; n++)
		{
			String sound_name = sound_name_list[n];
			ServerProxy.sending_data_manager.send_sound_data_to_player(player, sound_name);
		}
		
	}
	
	public void save_player_data(EntityPlayer player)
	{
		String player_name = player.getCommandSenderName();
		PlayerData player_data = get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		
		NBTTagCompound player_skill_level_data = data.getCompoundTag("skill_level_data");
		ServerProxy.file_manager.save_player_file(player_skill_level_data, "player_skill_level", player_name, player);
		
		NBTTagCompound player_skill_slot_data = data.getCompoundTag("skill_slot");
		ServerProxy.file_manager.save_player_file(player_skill_slot_data, "player_skill_slot", player_name, player);
		
		NBTTagCompound stat_data = player_data.player_stat.write_stat_to_nbt();
		ServerProxy.file_manager.save_player_file(stat_data, "player_stat", player_name, player);
	
		NBTTagCompound player_potion_data = data.getCompoundTag("potion_tree_data");
		ServerProxy.file_manager.save_player_file(player_potion_data, "player_potion_tree", player_name, player);
	}
}
