package com.abnull.yrs.network.mod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import io.netty.buffer.ByteBuf;

import com.abnull.yrs.network.mod.handler.AbstractServerMessageHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ServerDataMessage implements IMessage {

	public NBTTagCompound message_data;
	
	public ServerDataMessage()
	{
		
	}
	
	public ServerDataMessage(NBTTagCompound m)
	{
		this.message_data = m;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.message_data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.message_data);
	}
	
	public static class Handler extends AbstractServerMessageHandler<ServerDataMessage>
	{
		@SideOnly(Side.SERVER)
		@Override
		public IMessage handleServerMessage(EntityPlayer player, ServerDataMessage message , MessageContext ctx)
		{
			String player_name = player.getCommandSenderName();
			
			// Action Data From Client
			String action_name = message.message_data.getString("action_name");
			NBTTagCompound action_data = message.message_data.getCompoundTag("action_data");
			boolean is_nbt = message.message_data.getBoolean("is_nbt");
			
			if(!is_nbt)
			{
				// Events
				if(action_name.equals("upgrade_stat"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionUpgradeStatEvent(player, player_name, action_name, action_data));
				}
				else if(action_name.equals("open_gui_screen"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionOpenGuiScreenEvent(player, player_name, action_name, action_data));
				}
				else if(action_name.equals("set_skill_slot"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionSetSkillSlotEvent(player, player_name, action_name, action_data));
				}
				else if(action_name.equals("upgrade_skill_level"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionUpgradeSkillLevelEvent(player, player_name, action_name, action_data));
				}
				else if(action_name.equals("use_skill"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionUseSkillEvent(player, player_name, action_name, action_data));
				}
				else if(action_name.equals("use_potion"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionUsePotionEvent(player, player_name, action_name, action_data));
				}
			}
			else
			{
				if(action_name.equals("update_potion_slot"))
				{
					MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.server.action.ActionUpdatePotionSlotEvent(player, player_name, action_name, action_data));
				}
			}
			return null;
		}
	}
}
