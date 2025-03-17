package com.abnull.yrs.proxy;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.event.handler.client.GameKeyInputEventHandler;
import com.abnull.yrs.event.handler.client.GuiKeyInputEventHandler;
import com.abnull.yrs.event.handler.client.RenderGuiScreenEventHandler;
import com.abnull.yrs.event.handler.client.ThePlayerEventHandler;
import com.abnull.yrs.network.mod.ClientSendingDataManager;
import com.abnull.yrs.util.SkillData;
import com.abnull.yrs.util.SoundHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy{

	public static ClientSendingDataManager client_sending_data_manager;
	
	public static NBTTagCompound player_data = new NBTTagCompound();
	public static NBTTagCompound skill_texture_data = new NBTTagCompound();
	public static HashMap<String, SkillData> skill_data_map = new HashMap<String, SkillData>();
	public static HashMap<String, Long> skill_cooltime_map = new HashMap<String, Long>();
	public static HashMap<Integer, Integer> granted_effect_duration_map = new HashMap<Integer, Integer>();
	public static HashMap<String, byte[]> sound_data_map = new HashMap<String, byte[]>();
	
	public static RenderGuiScreenEventHandler render_gui_screen_event_handler;
	public static GuiKeyInputEventHandler gui_key_input_event_handler;
	public static GameKeyInputEventHandler game_key_input_event_handler;
	public static ThePlayerEventHandler the_player_event_handler;
	
	public ClientProxy()
	{
		client_sending_data_manager = new ClientSendingDataManager(YRSMod.socket);
		
		render_gui_screen_event_handler = new RenderGuiScreenEventHandler();
		gui_key_input_event_handler = new GuiKeyInputEventHandler();
		game_key_input_event_handler = new GameKeyInputEventHandler();
		the_player_event_handler = new ThePlayerEventHandler();
		
		event_handler_registry(render_gui_screen_event_handler);
		event_handler_registry(gui_key_input_event_handler);
		event_handler_registry(game_key_input_event_handler);
		event_handler_registry(the_player_event_handler);
	}

	@Override
	public void pre_init(FMLPreInitializationEvent event) {
		YRSMod.the_player = Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void post_init(FMLPostInitializationEvent event) {
		
	}
	
	public void event_handler_registry(Object target)
	{
		MinecraftForge.EVENT_BUS.register(target);
		FMLCommonHandler.instance().bus().register(target);
	}
}
