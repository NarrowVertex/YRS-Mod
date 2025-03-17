package com.abnull.yrs.network.plugin.eventhandle;

import com.abnull.yrs.network.plugin.PluginData;

public interface IPluginEventHandle
{
	public void init(PluginData plugin_data);
	
	public void recv(PluginData plugin_data, String label, String contents);
}