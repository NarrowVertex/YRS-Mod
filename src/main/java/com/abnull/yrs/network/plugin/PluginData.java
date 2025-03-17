package com.abnull.yrs.network.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import net.minecraftforge.common.MinecraftForge;

import com.abnull.yrs.event.server.plugindata.PluginDataRecvEvent;
import com.abnull.yrs.network.plugin.eventhandle.IPluginEventHandle;
import com.abnull.yrs.network.plugin.eventhandle.PluginEventHandleManager;
import com.abnull.yrs.proxy.ServerProxy;

public class PluginData {

	public String plugin_name;
	public IPluginEventHandle plugin_event_handle;
	
	Socket socket;
	
	BufferedWriter output;
	BufferedReader input;
	
	public PluginData(String n, Socket s, BufferedWriter o, BufferedReader i)
	{
		plugin_name = n;
		plugin_event_handle = ServerProxy.plugin_event_handle_manager.get_plugin_event_handle(plugin_name);
		
		socket = s;
		
		output = o;
		input = i;
		
		receive_plugin_data_thread rpdt = new receive_plugin_data_thread(plugin_name, this, input);
		rpdt.start();
	}
	
	public void send_message(String label, String contents)
	{
		try {
			output.write(label + ":" + contents + "\r\n");
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send_message(String label, String[] contents_array)
	{
		String contents = "";
		for(int n = 0; n < contents_array.length; n++)
		{
			contents += contents_array[n] + ",";
		}
		
		try {
			output.write(label + ":" + contents + "\r\n");
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class receive_plugin_data_thread extends Thread
{
	String plugin_name;
	PluginData plugin_data;
	BufferedReader input;
	
	public receive_plugin_data_thread(String name, PluginData data, BufferedReader i)
	{
		plugin_name = name;
		plugin_data = data;
		input = i;
	}
	
	public void run()
	{
		String data;
		
		try{
			while((data = input.readLine()) != null)
			{
				String label = data.split(":", 2)[0];
				String contents = data.split(":", 2)[1];
				
				MinecraftForge.EVENT_BUS.post(new PluginDataRecvEvent(plugin_name, plugin_data, label, contents));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}