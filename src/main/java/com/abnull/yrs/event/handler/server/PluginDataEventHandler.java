package com.abnull.yrs.event.handler.server;

import com.abnull.yrs.event.server.plugindata.PluginDataInitEvent;
import com.abnull.yrs.event.server.plugindata.PluginDataRecvEvent;
import com.abnull.yrs.network.plugin.PluginData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PluginDataEventHandler {

	public PluginDataEventHandler()
	{
		
	}
	
	@SubscribeEvent
	public void onPluginDataInit(PluginDataInitEvent event)
	{
		String plugin_name = event.plugin_name;
		PluginData plugin_data = event.plugin_data;
		
		plugin_data.plugin_event_handle.init(plugin_data);
	}
	
	@SubscribeEvent
	public void onPluginDataRecv(PluginDataRecvEvent event)
	{
		PluginData plugin_data = event.plugin_data;
		String label = event.label;
		String contents = event.contents;
		
		plugin_data.plugin_event_handle.recv(plugin_data, label, contents);
	}
}
