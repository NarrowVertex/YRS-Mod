package com.abnull.yrs.event.server.plugindata;

import com.abnull.yrs.network.plugin.PluginData;

public class PluginDataInitEvent extends PluginDataEvent {

	public PluginDataInitEvent(String pn, PluginData pd) {
		super(pn, pd);
	}
}
