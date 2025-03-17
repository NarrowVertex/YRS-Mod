package com.abnull.yrs.network.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import net.minecraftforge.common.MinecraftForge;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.event.server.plugindata.PluginDataInitEvent;
import com.abnull.yrs.proxy.ServerProxy;

public class PluginCommunicationManager {
	
	public ServerSocket server_socket;
	
	public HashMap<String, PluginData> plugin_data_map = new HashMap<String, PluginData>();
	
	public PluginCommunicationManager(int port)
	{
		try {
			YRSMod.debug.print_debug("PluginCommuicationManager", 26, "Open Server With Port[" + port + "]. . .");
			server_socket = new ServerSocket(port);
			start_connection();
		} catch (Exception e) {
			YRSMod.debug.print_error("PluginCommuicationManager", 30, 000, "We Have Some Error With Opening Server. . .");
			e.printStackTrace();
		}
	}
	
	public void start_connection()
	{
		accept_socket as = new accept_socket(server_socket);
		as.start();
	}
	
	public PluginData get_plugin_data(String name)
	{
		PluginData plugin_data = plugin_data_map.get(name);
		if(plugin_data == null)
		{
			System.out.println("[YRS Mod Server] Plugin [" + name + "] Couldn't Find. . .");
			System.out.println("[YRS Mod Server] Map : " + plugin_data_map.toString() + ", Size : " + plugin_data_map.size());
			return null;
		}
		return plugin_data_map.get(name);
	}
}

class accept_socket extends Thread
{
	ServerSocket server_socket;
	
	public accept_socket(ServerSocket socket)
	{
		server_socket = socket; 
	}
	
	public void run()
	{
		try {
			while(true)
			{
				Socket socket = server_socket.accept();

				receive_socket_data rsd = new receive_socket_data(socket);
				rsd.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class receive_socket_data extends Thread
{
	Socket socket;
	
	BufferedWriter output;
	BufferedReader input;
	
	public receive_socket_data(Socket s)
	{
		socket = s;
		
		try {
			output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		String data;
		
		try{
			while((data = input.readLine()) != null)
			{
				String label = data.split(":", 2)[0];
				String contents = data.split(":", 2)[1];
				if(label.equals("PN"))
				{
					YRSMod.debug.print_debug("PluginCommunication", 110, "New Plugin Contacted With Name:" + contents);
					PluginData plugin_data = new PluginData(contents, socket, output, input);
					ServerProxy.plugin_communication_manager.plugin_data_map.put(contents, plugin_data);
					
					YRSMod.debug.print_debug("PluginCommunication", 114, "Initializing New PluginData With Name:" + contents);
					MinecraftForge.EVENT_BUS.post(new PluginDataInitEvent(contents, plugin_data));
					
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
