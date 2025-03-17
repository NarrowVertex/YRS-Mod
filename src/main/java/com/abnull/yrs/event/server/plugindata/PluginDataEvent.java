package com.abnull.yrs.event.server.plugindata;

import com.abnull.yrs.network.plugin.PluginData;

import cpw.mods.fml.common.eventhandler.Event;

public abstract class PluginDataEvent extends Event{

	public String plugin_name;
	public PluginData plugin_data;
	
	public PluginDataEvent(String pn, PluginData pd)
	{
		plugin_name = pn;
		plugin_data = pd;
	}
	
}
