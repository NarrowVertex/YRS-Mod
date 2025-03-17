package com.abnull.yrs;

import net.minecraft.entity.player.EntityPlayer;

import com.abnull.yrs.command.CommandYRS;
import com.abnull.yrs.customgui.handler.CustomGuiHandler;
import com.abnull.yrs.item.handler.RegisterItemHandler;
import com.abnull.yrs.log.LogDebugger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = YRSMod.MODID, version = YRSMod.VERSION)
public class YRSMod {

	public static YRSMod instance;
	
	public static final String MODID = "YRS Mod Recreate";
	public static final String VERSION = "1.0";
	
    public static final SimpleNetworkWrapper socket = NetworkRegistry.INSTANCE.newSimpleChannel(YRSMod.MODID);
	
    public static EntityPlayer the_player;
    
    public static LogDebugger debug;
    
	
	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void pre_init_client(FMLPreInitializationEvent event)
	{
		debug = new LogDebugger(true);
		new com.abnull.yrs.proxy.ClientProxy().pre_init(event);
	}
	
	@SideOnly(Side.SERVER)
	@Mod.EventHandler
	public void pre_init_server(FMLPreInitializationEvent event)
	{
		debug = new LogDebugger(false);
		new com.abnull.yrs.proxy.ServerProxy().pre_init(event);
	}
	
	@SideOnly(Side.SERVER)
	@Mod.EventHandler
	public void server_starting(FMLServerStartingEvent event) 
	{
		event.registerServerCommand(new CommandYRS());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CustomGuiHandler());
		new RegisterItemHandler().register();
		
		instance = this;
		
		socket.registerMessage(com.abnull.yrs.network.mod.ServerDataMessage.Handler.class, com.abnull.yrs.network.mod.ServerDataMessage.class, 0, Side.SERVER);
		socket.registerMessage(com.abnull.yrs.network.mod.ClientDataMessage.Handler.class, com.abnull.yrs.network.mod.ClientDataMessage.class, 1, Side.CLIENT);
	}
}
