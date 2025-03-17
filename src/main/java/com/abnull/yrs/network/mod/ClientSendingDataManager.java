package com.abnull.yrs.network.mod;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ClientSendingDataManager {
	
	SimpleNetworkWrapper socket;
	
	public ClientSendingDataManager(SimpleNetworkWrapper s)
	{
		socket = s;
	}
	
	public void send_data_to_server(String action_name, String[][] action_contents)
	{
		NBTTagCompound action = new NBTTagCompound();
		action.setString("action_name", action_name);
		
		NBTTagCompound action_data = new NBTTagCompound();
		for(int n = 0; n < action_contents.length; n++)
		{
			action_data.setString(action_contents[n][0], action_contents[n][1]);
		}
		action.setTag("action_data", action_data);
		action.setBoolean("is_nbt", false);
		socket.sendToServer(new ServerDataMessage(action));
	}
	
	public void send_data_to_server(String action_name, NBTTagCompound action_contents)
	{
		NBTTagCompound action = new NBTTagCompound();
		action.setString("action_name", action_name);
		action.setTag("action_data", action_contents);
		action.setBoolean("is_nbt", true);
		socket.sendToServer(new ServerDataMessage(action));
	}
}
