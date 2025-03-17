package com.abnull.yrs.event.handler.server;

import com.abnull.yrs.player.data.PlayerData;
import com.abnull.yrs.player.util.PlayerBasicDataManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.customgui.handler.CustomGuiHandler;
import com.abnull.yrs.event.server.player.PlayerEnterRegionEvent;
import com.abnull.yrs.event.server.player.PlayerExitRegionEvent;
import com.abnull.yrs.event.server.player.PlayerLevelUpEvent;
import com.abnull.yrs.event.server.plugindata.PluginDataRecvEvent;
import com.abnull.yrs.network.mod.ClientDataMessage;
import com.abnull.yrs.proxy.ServerProxy;
import com.abnull.yrs.region.Region;
import com.abnull.yrs.util.MathHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class IngameEventHandler {

	public IngameEventHandler()
	{
		
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = player.getCommandSenderName();
		
		ServerProxy.player_data_manager.add_player_data(player);
		
		NBTTagCompound player_data = ServerProxy.player_data_manager.get_player_data(player).data;
		ServerProxy.file_manager.load_player_file(player_data, "player_data", player_name, player);
		
		ServerProxy.sending_data_manager.send_all_data_to_player(player, player_data);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		String player_name = player.getCommandSenderName();
		
		if(ServerProxy.player_data_manager.has_player_data(player))
		{
			NBTTagCompound player_data = ServerProxy.player_data_manager.get_player_data(player).data;
			
			ServerProxy.file_manager.save_player_file(player_data, "player_data", player_name, player);
			ServerProxy.player_data_manager.remove_player_data(player);
		}
		else
		{
			return;
		}
	}
	
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		event.entityLiving.isDead = true;
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data(player);
		NBTTagCompound player_nbt_data = player_data.data;
		
		// Player HP & MP Regeneration System
		if(player.isDead)
			return;
		
		float player_health = player.getHealth();
		int player_mana = Integer.parseInt(player_nbt_data.getString("mp"));
		
		long current_time = System.currentTimeMillis();
		
		long pre_hp_regen_time = player_data.pre_hp_regen_time;
		long pre_mp_regen_time = player_data.pre_mp_regen_time;
		
		float hp_regen_speed = Float.parseFloat(player_nbt_data.getString("hp-regen-speed"));
		float mp_regen_speed = Float.parseFloat(player_nbt_data.getString("mp-regen-speed"));
		
		float hp_regen_amount = Float.parseFloat(player_nbt_data.getString("hp-regen-amount"));
		int mp_regen_amount = Integer.parseInt(player_nbt_data.getString("mp-regen-amount"));
		
		if((current_time - pre_hp_regen_time) >= hp_regen_speed * 1000)
		{
			PlayerBasicDataManager.set_health(player, player.getHealth() + hp_regen_amount);
			player_data.pre_hp_regen_time = current_time;
			ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "hp");
		}
		
		if((current_time - pre_mp_regen_time) >= mp_regen_speed * 1000)
		{
			PlayerBasicDataManager.set_mana(player_nbt_data, player_mana + mp_regen_amount);
			player_data.pre_mp_regen_time = current_time;
			ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "mp");
		}
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "max-hp,max-mp");
		
		
		// Region System
		boolean is_player_in_region = false;
		for(Iterator iterator = ServerProxy.region_manager.get_region_iterator(); iterator.hasNext();)
		{
			Region region = (Region) iterator.next();
			if(region.is_player_in_region(player))
			{
				player_data.player_current_region = region;
				Region player_last_region = player_data.player_last_region;
				
				if(!region.get_region_name().equals(player_last_region.get_region_name()))
				{
					MinecraftForge.EVENT_BUS.post(new PlayerEnterRegionEvent(player, player_data, region));
					player_data.player_last_region = region;
				}
				is_player_in_region = true;
				break;
			}
		}
		if(!player_data.player_current_region.get_region_name().equals("None") && !is_player_in_region)
		{
			MinecraftForge.EVENT_BUS.post(new PlayerExitRegionEvent(player, player_data, player_data.player_current_region));
			player_data.player_current_region = new Region("None", null);
			player_data.player_last_region = new Region("None", null);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		EntityPlayer player = event.player;
		PlayerData player_data = ServerProxy.player_data_manager.get_player_data(player);
		NBTTagCompound player_nbt_data = player_data.data;
		
		float max_health = Float.parseFloat(player_nbt_data.getString("max-hp"));
		
		PlayerBasicDataManager.set_max_health(player, max_health);
		PlayerBasicDataManager.set_health(player, max_health);
		
		int max_mana = Integer.parseInt(player_nbt_data.getString("max-mp"));
		
		PlayerBasicDataManager.set_max_mana(player_nbt_data, max_mana);
		PlayerBasicDataManager.set_mana(player_nbt_data, max_mana);
		
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "max-hp,max-mp");
	}
	
	@SubscribeEvent
	public void onPlayerPickupXp(PlayerPickupXpEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		PlayerData data_player = ServerProxy.player_data_manager.get_player_data(player);
		NBTTagCompound player_data = data_player.data;
		NBTTagCompound job_data = data_player.last_job_data;
		
		int amount = event.orb.getXpValue();
		
		int pre_level = Integer.parseInt(player_data.getString("exp-level"));
		int level = Integer.parseInt(player_data.getString("exp-level"));
		long max_exp = Long.parseLong(player_data.getString("max-exp"));
		long exp = Long.parseLong(player_data.getString("exp"));
		
		exp += amount;
		
		while(exp >= max_exp)
		{
			exp -= max_exp;
			level++;
			
			String exp_equation = job_data.getString("exp-level-up-equation");
			if(exp_equation.contains("'exp'"))
				exp_equation = exp_equation.replaceAll("'exp'", "" + job_data.getString("exp"));
			if(exp_equation.contains("'level'"))
				exp_equation = exp_equation.replaceAll("'level'", "" + level);
			
			max_exp = (long) MathHelper.equation_calculate(exp_equation);
		}
		
		player.experience = 0;
		player.experienceTotal = 0;
		player.experienceLevel = level;
				
		player_data.setString("exp-level", "" + level);
		player_data.setString("max-exp", "" + max_exp);
		player_data.setString("exp", "" + exp);
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_data, "exp-level,max-exp,exp");
		
		if(level > pre_level)
		{
			MinecraftForge.EVENT_BUS.post(new PlayerLevelUpEvent(player, data_player, pre_level, level));
		}
		// Print Xp Amount
		// Result : Just Event Appear When Get XP Orb, Not By XP Command
		// Test With Old Version(Plugin Event)
		// Result : It is same with this event
		// Total Result : This Event
	}
	
	@SubscribeEvent
	public void onPlayerLevelUp(PlayerLevelUpEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		PlayerData data_player = event.player_data;
		NBTTagCompound player_data = data_player.data;
		NBTTagCompound job_data = data_player.last_job_data;
		
		int pre_level = event.pre_level;
		int level = event.level;
		
		double max_hp = Float.parseFloat(job_data.getString("hp"));
		String max_hp_equation = job_data.getString("hp-level-up-equation");
		if(max_hp_equation.contains("'hp'"))
			max_hp_equation = max_hp_equation.replaceAll("'hp'", "" + max_hp);
		if(max_hp_equation.contains("'level'"))
			max_hp_equation = max_hp_equation.replaceAll("'level'", "" + level);
		max_hp = MathHelper.equation_calculate(max_hp_equation);
		player_data.setString("max-hp", "" + max_hp);
		event.entityPlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(max_hp);
		
		int max_mp = Integer.parseInt(job_data.getString("mp"));
		String max_mp_equation = job_data.getString("mp-level-up-equation");
		if(max_mp_equation.contains("'mp'"))
			max_mp_equation = max_mp_equation.replaceAll("'mp'", "" + max_mp);
		if(max_mp_equation.contains("'level'"))
			max_mp_equation = max_mp_equation.replaceAll("'level'", "" + level);
		max_mp = (int) MathHelper.equation_calculate(max_mp_equation);
		player_data.setString("max-mp", "" + max_mp);
		
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_data, "max-hp,max-mp");
	}
	
	@SubscribeEvent
	public void onPlayerEnterRegion(PlayerEnterRegionEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		PlayerData player_data = event.player_data;
		NBTTagCompound player_nbt_data = player_data.data;
		Region region = event.region;
		
		player.addChatMessage(new ChatComponentText("Enter To " + region.get_region_name() + ". . ."));
		
		player_nbt_data.setString("region", region.get_region_name());
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "region");
	}
	
	@SubscribeEvent
	public void onPlayerExitRegion(PlayerExitRegionEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		PlayerData player_data = event.player_data;
		NBTTagCompound player_nbt_data = player_data.data;
		Region region = event.region;
		
		player.addChatMessage(new ChatComponentText("Exit To " + region.get_region_name() + ". . ."));
		
		player_nbt_data.setString("region", "None");
		ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_nbt_data, "region");
	}
}
