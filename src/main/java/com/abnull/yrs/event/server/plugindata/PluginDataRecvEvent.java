package com.abnull.yrs.event.server.plugindata;

import com.abnull.yrs.network.plugin.PluginData;

public class PluginDataRecvEvent extends PluginDataEvent {

	public String label;
	public String contents;
	
	public PluginDataRecvEvent(String name, PluginData data, String l, String c)
	{
		super(name, data);
		
		label = l;
		contents = c;
	}
	
}
