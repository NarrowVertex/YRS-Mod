package com.abnull.yrs.network.plugin.eventhandle;

import net.minecraft.nbt.NBTTagCompound;

import com.abnull.yrs.network.plugin.PluginData;
import com.abnull.yrs.proxy.ServerProxy;

public class YRSModHelpPluginEventHandle implements IPluginEventHandle {

	@Override
	public void init(PluginData plugin_data)
	{
		NBTTagCompound stat_config_data = ServerProxy.stat_config_data;
		String str_strength = stat_config_data.getString("str-strength");
		String def_defence = stat_config_data.getString("def-defence");
		String dex_probability = stat_config_data.getString("dex-probability");
		String dex_critical_multiple = stat_config_data.getString("dex-critical-multiple");
		String agi_probability = stat_config_data.getString("agi-probability");
		String acc_accuracy = stat_config_data.getString("acc-accuracy");
		plugin_data.send_message("SD", new String[]{ str_strength, def_defence, dex_probability, dex_critical_multiple, agi_probability, acc_accuracy });
	}

	@Override
	public void recv(PluginData plugin_data, String label, String contents)
	{
		
	}
}
