package com.abnull.yrs.command;

import java.util.Collection;
import java.util.Iterator;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.player.data.PlayerData;
import com.abnull.yrs.proxy.ServerProxy;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandYRS extends CommandBase {

	String[] player_component_data_names = { "max-hp", "hp", "max-mp", "mp", "hp-regen-speed", "hp-regen-amount", "mp-regen-speed", "mp-regen-amount", "exp-level", "max-exp", "exp", "skill-point", "stat-point", "jobs" };

	@Override
	public String getCommandName()
	{
		return "yrs";
	}

	public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) 
	{
		return "commands.yrs.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
	{
		if(p_71515_2_.length == 1) 
		{
			if(p_71515_2_[0].equals("reload"))
			{
				ServerProxy.loading_nbt_data();
				
				YRSMod.debug.print_debug("CommandYRS", 55, "Loading Player Data. . .");
				for(String player_name : ServerProxy.player_data_manager.get_player_name_list())
				{
					PlayerData player_data = ServerProxy.player_data_manager.get_player_data_by_name(player_name);
					NBTTagCompound player_nbt_data = player_data.data;
					EntityPlayer player = player_data.player;
					
					ServerProxy.file_manager.save_player_file(player_nbt_data, "player_data", player_name, player);
					ServerProxy.player_data_manager.remove_player_data(player);
					
					ServerProxy.player_data_manager.add_player_data(player);
					NBTTagCompound player_new_nbt_data = ServerProxy.player_data_manager.get_player_data(player).data;
					
					ServerProxy.file_manager.load_player_file(player_new_nbt_data, "player_data", player_name, player);
					
					ServerProxy.sending_data_manager.send_all_data_to_player(player, player_new_nbt_data);
				}
				YRSMod.debug.print_debug("CommandYRS", 72, "Loaded Player Data. . !");
				
				YRSMod.debug.print_debug("CommandYRS", 74, "Reloading Complement. . !");
			}
		}
		else if(p_71515_2_.length == 2) 
		{
			if(p_71515_2_[0].equals("getvalue"))
			{
				EntityPlayerMP player = getPlayer(p_71515_1_, p_71515_2_[1]);
				if(player == null)
		            throw new WrongUsageException("commands.yrs.usage", new Object[0]);
				
				NBTTagCompound player_data = ServerProxy.player_data_manager.get_player_data(player).data;
				
				
				player.addChatMessage(new ChatComponentText("==========[" + p_71515_2_[1] + "]=========="));
				for(int n = 0; n < player_component_data_names.length; n++) 
				{
					String data_name = player_component_data_names[n];
					String data_value = player_data.getString(data_name);
					
					player.addChatMessage(new ChatComponentText("-" + data_name + " : " + data_value));
				}
			}
		}
		else if(p_71515_2_.length == 4)
		{
			if(p_71515_2_[0].equals("setvalue"))
			{
				EntityPlayerMP player = getPlayer(p_71515_1_, p_71515_2_[1]);
				if(player == null)
		            throw new WrongUsageException("commands.yrs.usage", new Object[0]);
				NBTTagCompound player_data = ServerProxy.player_data_manager.get_player_data(player).data;
				
				String value_name = p_71515_2_[2];
				String value = p_71515_2_[3];
				
				if(!player_data.hasKey(value_name))
		            throw new WrongUsageException("commands.yrs.usage", new Object[0]);
				
				player_data.setString(value_name, value);
				ServerProxy.sending_data_manager.send_each_data_to_player_string(player, player_data, value_name);
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + p_71515_2_[0] + "'s Data is Successfully Edited. . !"));
			}
		}
	}
}
