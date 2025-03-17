package com.abnull.yrs.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.command.CommandYRS;
import com.abnull.yrs.event.handler.server.ClientActionEventHandler;
import com.abnull.yrs.event.handler.server.IngameEventHandler;
import com.abnull.yrs.event.handler.server.PluginDataEventHandler;
import com.abnull.yrs.file.FileManager;
import com.abnull.yrs.item.handler.RegisterItemHandler;
import com.abnull.yrs.network.mod.ServerSendingDataManager;
import com.abnull.yrs.network.plugin.PluginCommunicationManager;
import com.abnull.yrs.network.plugin.eventhandle.PluginEventHandleManager;
import com.abnull.yrs.player.data.PlayerDataManager;
import com.abnull.yrs.region.RegionManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ServerProxy implements IProxy {

	public static FileManager file_manager;
	public static PlayerDataManager player_data_manager;
	public static ServerSendingDataManager sending_data_manager;
	public static PluginCommunicationManager plugin_communication_manager;
	public static PluginEventHandleManager plugin_event_handle_manager;
	public static RegionManager region_manager;
	
	public ClientActionEventHandler client_action_event_handler;
	public IngameEventHandler ingame_event_handler;
	public PluginDataEventHandler plugin_data_event_handler;
	
	public static NBTTagCompound config_data = new NBTTagCompound();
	public static NBTTagCompound stat_config_data = new NBTTagCompound();
	public static NBTTagCompound job_data = new NBTTagCompound();
	public static NBTTagCompound skill_data = new NBTTagCompound();
	public static NBTTagCompound skill_texture_data = new NBTTagCompound();
	public static NBTTagCompound region_data = new NBTTagCompound();
	public static NBTTagCompound background_sound_data = new NBTTagCompound();
	
	public static ServerSocket plugin_connection_socket;
	
	public ServerProxy()
	{
		YRSMod.debug.print_debug("ServerProxy", 54, "ManagerClass Is Initializing. . .");
		file_manager = new FileManager();
		player_data_manager = new PlayerDataManager();
		sending_data_manager = new ServerSendingDataManager(YRSMod.socket);
		plugin_communication_manager = new PluginCommunicationManager(25566);
		plugin_event_handle_manager = new PluginEventHandleManager();
		region_manager = new RegionManager();
		YRSMod.debug.print_debug("ServerProxy", 61, "ManagerClass Has Initialized. . !");
		
		YRSMod.debug.print_debug("ServerProxy", 63, "EventHandler Is Initializing. . .");
		client_action_event_handler = new ClientActionEventHandler();
		ingame_event_handler = new IngameEventHandler();
		plugin_data_event_handler = new PluginDataEventHandler();
		YRSMod.debug.print_debug("ServerProxy", 67, "EventHandler Has Initialized. . !");
		
		YRSMod.debug.print_debug("ServerProxy", 69, "EventHandler Is Registrying. . .");
		event_handler_registry(client_action_event_handler);
		event_handler_registry(ingame_event_handler);
		event_handler_registry(plugin_data_event_handler);
		YRSMod.debug.print_debug("ServerProxy", 73, "EventHandler Has Registryed. . !");
	}
	
	@Override
	public void pre_init(FMLPreInitializationEvent event)
	{
		loading_nbt_data();
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
    	
	}
	
	@Override
	public void post_init(FMLPostInitializationEvent event)
	{
		
	}
	
	public static void loading_nbt_data()
	{
		YRSMod.debug.print_debug("ServerProxy", 96, "Loading Data Files. . .");
		init_nbt_data();
		file_manager.load_file(config_data, "config");
		file_manager.load_file(stat_config_data, "stat_config");
		file_manager.load_file(skill_data, "skill");
		file_manager.load_file(job_data, "job");
		file_manager.load_file(skill_texture_data, "skill_texture");
		file_manager.load_file(region_data, "region");
		file_manager.load_file(background_sound_data, "background_sound");
		YRSMod.debug.print_debug("ServerProxy", 105, "Loaded Data Files. . !");
		
		region_manager.init(region_data);
	}
	
	public static void init_nbt_data()
	{
		config_data = new NBTTagCompound();
		stat_config_data = new NBTTagCompound();
		job_data = new NBTTagCompound();
		skill_data = new NBTTagCompound();
		skill_texture_data = new NBTTagCompound();
		region_data = new NBTTagCompound();
		background_sound_data = new NBTTagCompound();
	}
	
	public void event_handler_registry(Object target)
	{
		MinecraftForge.EVENT_BUS.register(target);
		FMLCommonHandler.instance().bus().register(target);
	}
}
