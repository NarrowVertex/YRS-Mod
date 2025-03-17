package com.abnull.yrs.event.handler.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.customgui.handler.CustomGuiHandler;
import com.abnull.yrs.event.server.action.ActionOpenGuiScreenEvent;
import com.abnull.yrs.event.server.action.ActionSetSkillSlotEvent;
import com.abnull.yrs.event.server.action.ActionUpdatePotionSlotEvent;
import com.abnull.yrs.event.server.action.ActionUpgradeSkillLevelEvent;
import com.abnull.yrs.event.server.action.ActionUpgradeStatEvent;
import com.abnull.yrs.event.server.action.ActionUsePotionEvent;
import com.abnull.yrs.event.server.action.ActionUseSkillEvent;
import com.abnull.yrs.item.potion.ItemYRSPotion;
import com.abnull.yrs.player.data.PlayerData;
import com.abnull.yrs.player.slot.skill.PlayerSkillSlot;
import com.abnull.yrs.player.stat.PlayerStat;
import com.abnull.yrs.proxy.ServerProxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientActionEventHandler {

	@SubscribeEvent
	public void onOpenGuiScreen(ActionOpenGuiScreenEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		
		NBTTagCompound action_data = event.action_data;
		
		String gui_screen_name = action_data.getString("gui_screen_name");
		
		if(gui_screen_name.equals("potion tree"))
		{
			player.openGui(YRSMod.instance, CustomGuiHandler.POTION_SLOT_TILE_ENTITY_GUI, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}
	
	@SubscribeEvent
	public void onUpgradeStat(ActionUpgradeStatEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		PlayerStat player_stat = player_data.player_stat;
		NBTTagCompound action_data = event.action_data;
		String stat_name = action_data.getString("name");
		
		int stat_point = Integer.parseInt(data.getString("stat-point"));
		
		if(stat_point > 0)
		{
			if(stat_name.equals("str"))
				player_stat.str++;
			else if(stat_name.equals("def"))
				player_stat.def++;
			else if(stat_name.equals("acc"))
				player_stat.acc++;
			else if(stat_name.equals("agi"))
				player_stat.agi++;
			
			stat_point--;
			
			NBTTagCompound stat_data = player_stat.write_stat_to_nbt();
			data.setTag("stat", stat_data);
			data.setString("stat-point", "" + stat_point);
			
			ServerProxy.sending_data_manager.send_each_data_to_player_string(player, data, "stat-point");
			ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, data, "stat");
			
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
		}
	}
	
	@SubscribeEvent
	public void onUpgradePotionSlot(ActionUpdatePotionSlotEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		NBTTagCompound action_data = event.action_data;
		
		NBTTagCompound potion_tree_data = data.getCompoundTag("potion_tree_data");
		for(int n = 0; n < 3; n++)
		{
			NBTTagCompound potion_data = potion_tree_data.getCompoundTag("" + n);
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
		
		data.setTag("potion_tree_data", action_data);
	}
	
	@SubscribeEvent
	public void onUsePotionSlot(ActionUsePotionEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		NBTTagCompound action_data = event.action_data;
		
		int potion_slot_number = Integer.parseInt(action_data.getString("potion_slot_num"));
		ItemStack potion_item_stack = player_data.player_potion_slot[potion_slot_number];
		
		if(potion_item_stack == null)
			return;
		
		if(!(potion_item_stack.getItem() instanceof ItemYRSPotion))
			return;
		
		potion_item_stack.useItemRightClick(player.getEntityWorld(), player);
		
		if(potion_item_stack.stackSize <= 0)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("is_null", true);
			nbt.setTag("data", new NBTTagCompound());
			data.getCompoundTag("potion_tree_data").setTag("" + potion_slot_number, nbt);
		}
		else
		{
			NBTTagCompound slot_data = new NBTTagCompound();
			NBTTagCompound potion_data = new NBTTagCompound();
			potion_item_stack.writeToNBT(potion_data);
			slot_data.setTag("data", potion_data);
			slot_data.setBoolean("is_null", false);
			data.getCompoundTag("potion_tree_data").setTag("" + potion_slot_number, slot_data);
		}
		
		ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, data, "potion_tree_data");
	}
	
	@SubscribeEvent
	public void onSetSkillSlot(ActionSetSkillSlotEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		NBTTagCompound action_data = event.action_data;
		
		int slot_number = Integer.parseInt(action_data.getString("slot_number"));
		String skill_name = action_data.getString("skill_name");
		
		PlayerSkillSlot slot = player_data.player_skill_slot[slot_number];
		NBTTagCompound skill_data = ServerProxy.skill_data.getCompoundTag(skill_name);
		
		// Check Whether Player Has the Permission about this skill(Job, Stat, BaseSkill, PlayerLevel).
		
		if(!skill_name.equals("n"))
		{
			int skill_level = Integer.parseInt(data.getCompoundTag("skill_level_data").getString(skill_name));
			if(!has_skill_permission(player_data, data, skill_name, skill_data) || skill_level <= 0)
			{
				return;
			}
			
			for(int n = 0; n < 6; n++)
			{
				if(slot_number != n)
				{
					PlayerSkillSlot another_slot = player_data.player_skill_slot[n];
					if(skill_name.equals(another_slot.get_skill_name()))
						return;
				}
			}
		}
		
		slot.set_data(skill_data);
		data.getCompoundTag("skill_slot").setString("slot" + (slot_number + 1), skill_name);
		
		ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, data, "skill_slot");
	}
	
	@SubscribeEvent
	public void onUpgradeSkillLevel(ActionUpgradeSkillLevelEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		NBTTagCompound action_data = event.action_data;
		
		String skill_name = action_data.getString("skill_name");	
		NBTTagCompound skill_data = ServerProxy.skill_data.getCompoundTag(skill_name);
		
		if(!has_skill_permission(player_data, data, skill_name, skill_data))
			return;
		
		int skill_point = Integer.parseInt(data.getString("skill-point"));
		if(skill_point <= 0)
			return;
			
		int skill_master_level = Integer.parseInt(skill_data.getString("max-skill-level"));
		int skill_level = Integer.parseInt(data.getCompoundTag("skill_level_data").getString(skill_name));
		if(skill_level >= skill_master_level)
			return;
		
		skill_level += 1;
		skill_point -= 1;
		
		data.getCompoundTag("skill_level_data").setString(skill_name, "" + skill_level);
		data.setString("skill-point", "" + skill_point);
		
		ServerProxy.sending_data_manager.send_each_data_to_player_nbt(player, data, "skill_level_data");
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, data, "skill-point");
	}
	
	@SubscribeEvent
	public void onUseSkill(ActionUseSkillEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = event.player_name;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
		NBTTagCompound data = player_data.data;
		NBTTagCompound action_data = event.action_data;
		
		int skill_slot_number = Integer.parseInt(action_data.getString("skill_slot_num"));
		PlayerSkillSlot skill_slot = player_data.player_skill_slot[skill_slot_number];
		String skill_name = skill_slot.get_skill_name();
		NBTTagCompound skill_data = skill_slot.get_data();
		int skill_level = Integer.parseInt(data.getCompoundTag("skill_level_data").getString(skill_name));
		
		if(skill_data == null || skill_name.equals("n"))
		{
			return;
		}
		
		if(!has_skill_permission(player_data, data, skill_name, skill_data) || skill_level <= 0)
		{
			return;
		}
		
		long skill_cooltime = (long) (Float.parseFloat(skill_data.getString("cooltime")) * 1000);
		long current_skill_cooltime = player_data.get_skill_cooltime(skill_name);
		if((System.currentTimeMillis() - current_skill_cooltime) < skill_cooltime)
		{
			return;
		}
		
		int skill_cost_mp = Integer.parseInt(skill_data.getString("cost-mana"));
		int player_mp = Integer.parseInt(data.getString("mp"));
		if(player_mp < skill_cost_mp)
		{
			return;
		}
		data.setString("mp", "" + (player_mp - skill_cost_mp));
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, data, "mp");
		
		String command = skill_data.getString("command");
		String total_skill_command = command + "_LV" + skill_level;
		
		String is_level_command = skill_data.getString("is_level_command");
		
		player_data.set_skill_cooltime(skill_name);
		ServerProxy.plugin_communication_manager.get_plugin_data("YRSModHelpPlugin").send_message("CS", new String[]{ player_name, total_skill_command, is_level_command });
	}
	
	public boolean has_skill_permission(PlayerData player_data, NBTTagCompound data, String skill_name, NBTTagCompound skill_data)
	{		
		// Job
		String player_jobs_array = data.getString("jobs");
		String skill_need_job_name = skill_data.getString("job");
		if(!player_jobs_array.contains(skill_need_job_name))
			return false;
		
		// Stat
		if(skill_data.hasKey("need-stat"))
		{
			PlayerStat player_stat = player_data.player_stat;
			NBTTagCompound skill_need_stat_data = skill_data.getCompoundTag("need-stat");
			for(int n = 0; n < skill_need_stat_data.getInteger("tag_contents_count"); n++)
			{
				String contents = skill_need_stat_data.getString("" + n);
				String need_stat_name = contents.split(":")[0];
				int need_stat_value = Integer.parseInt(contents.split(":")[1]);
				int stat_value = 0;
				
				if(need_stat_name.equals("str"))
					stat_value = player_stat.str;
				else if(need_stat_name.equals("def"))
					stat_value = player_stat.def;
				else if(need_stat_name.equals("acc"))
					stat_value = player_stat.acc;
				else if(need_stat_name.equals("agi"))
					stat_value = player_stat.agi;
				
				if(stat_value < need_stat_value)
					return false;
			}
		}
		
		// Skill
		if(skill_data.hasKey("need-skill"))
		{
			String player_skill_name_array = data.getString("skill_array");
			NBTTagCompound skill_need_skill_data = data.getCompoundTag("need-skill");
			for(int n = 0; n < skill_need_skill_data.getInteger("tag_contents_count"); n++)
			{
				String need_skill_name = skill_need_skill_data.getString("" + n);
				if(!player_skill_name_array.contains(need_skill_name))
					return false;
			}
		}
		
		// Level
		if(skill_data.hasKey("need-level"))
		{
			int need_player_level = Integer.parseInt(skill_data.getString("need-level"));
			int player_level = Integer.parseInt(data.getString("exp-level"));
			if(player_level < need_player_level)
				return false;
		}
		
		return true;
	}
}
