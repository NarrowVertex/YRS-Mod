package com.abnull.yrs.network.mod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.proxy.ServerProxy;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ServerSendingDataManager {

	SimpleNetworkWrapper socket;
	
	public ServerSendingDataManager(SimpleNetworkWrapper s)
	{
		socket = s;
	}
	
	public void send_all_data_to_player(EntityPlayer player, NBTTagCompound data)
	{
		data.setBoolean("is_init", true);
		socket.sendTo(new ClientDataMessage(data), (EntityPlayerMP) player);
	}
	
	public void send_each_data_to_player_string(EntityPlayer player, NBTTagCompound data, String label_array)
	{
		NBTTagCompound send_data = new NBTTagCompound();
		send_data.setBoolean("is_init", false);
		send_data.setBoolean("is_string_data", true);
		send_data.setString("label_array", label_array);
		
		String[] labels = label_array.split(",");
		for(int n = 0; n < labels.length; n++)
		{
			String label = labels[n];
			send_data.setString(label, data.getString(label));
		}
		
		socket.sendTo(new ClientDataMessage(send_data), (EntityPlayerMP) player);
	}
	
	public void send_each_data_to_player_nbt(EntityPlayer player, NBTTagCompound data, String label_array)
	{
		NBTTagCompound send_data = new NBTTagCompound();
		send_data.setBoolean("is_init", false);
		send_data.setBoolean("is_string_data", false);
		send_data.setString("label_array", label_array);
		
		String[] labels = label_array.split(",");
		for(int n = 0; n < labels.length; n++)
		{
			String label = labels[n];
			send_data.setTag(label, data.getCompoundTag(label));
		}
		
		socket.sendTo(new ClientDataMessage(send_data), (EntityPlayerMP) player);
	}
	
	// less 10kb
	public void send_big_data_to_player(EntityPlayer player, String data_name, NBTTagCompound data, String label_array)
	{
		NBTTagCompound start_data = new NBTTagCompound();
		start_data.setString("start_data_name", data_name);
		socket.sendTo(new ClientDataMessage(start_data), (EntityPlayerMP)player);
		
		String[] labels = label_array.split(",");
		for(int n = 0; n < labels.length; n++)
		{
			String label = labels[n];
			socket.sendTo(new ClientDataMessage(data.getCompoundTag(label)), (EntityPlayerMP) player);
		}
		
		NBTTagCompound end_data = new NBTTagCompound();
		end_data.setString("end_data_name", "");
		socket.sendTo(new ClientDataMessage(end_data), (EntityPlayerMP) player);
	}
	
	//less 3mb
	public void send_sound_data_to_player(EntityPlayer player, String sound_name)
	{
		NBTTagCompound start_data = new NBTTagCompound();
		start_data.setString("start_sound_data_name", sound_name);
		socket.sendTo(new ClientDataMessage(start_data), (EntityPlayerMP)player);
		
		NBTTagCompound sound_data = ServerProxy.background_sound_data.getCompoundTag(sound_name);
		int count = sound_data.getInteger("count");
		for(int n = 0; n < count; n++)
		{
			NBTTagCompound data = new NBTTagCompound();
			byte[] byte_array = sound_data.getByteArray("" + n);
			for(int m = 0; m < byte_array.length; m++)
			{
				data.setByte("" + m, byte_array[m]);
			}
			data.setInteger("length", byte_array.length);
			data.setInteger("name", n);
			socket.sendTo(new ClientDataMessage(data), (EntityPlayerMP) player);
		}
		
		NBTTagCompound end_data = new NBTTagCompound();
		end_data.setString("end_sound_data_name", "");
		socket.sendTo(new ClientDataMessage(end_data), (EntityPlayerMP) player);
	}
}
