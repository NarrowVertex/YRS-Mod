package com.abnull.yrs.network.plugin.eventhandle;

import java.util.HashMap;

import com.abnull.yrs.network.plugin.PluginData;

public class PluginEventHandleManager {

	public HashMap<String, IPluginEventHandle> plugin_event_handle_map = new HashMap<String, IPluginEventHandle>();
	
	public PluginEventHandleManager()
	{
		plugin_event_handle_map.put("YRSModHelpPlugin", new YRSModHelpPluginEventHandle());
	}
	
	public IPluginEventHandle get_plugin_event_handle(String plugin_name)
	{
		return plugin_event_handle_map.get(plugin_name);
	}
}