package com.abnull.yrs.network.mod;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.customgui.CustomGuiIngame;
import com.abnull.yrs.network.mod.handler.AbstractClientMessageHandler;
import com.abnull.yrs.player.util.PlayerBasicDataManager;
import com.abnull.yrs.proxy.ClientProxy;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public class ClientDataMessage implements IMessage {
	
	public NBTTagCompound message_data;
	static boolean is_big_data_loading = false;
	static String big_data_name = "";
	
	public ClientDataMessage()
	{
		
	}
	
	public ClientDataMessage(NBTTagCompound m)
	{
		this.message_data = m;
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		this.message_data = ByteBufUtils.readTag(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteBufUtils.writeTag(buffer, this.message_data);
	}
	
	public static class Handler extends AbstractClientMessageHandler<ClientDataMessage>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public IMessage handleClientMessage(EntityPlayer player, ClientDataMessage message, MessageContext ctx)
		{
			// Client
			if(message == null)
				return null;
			
			if(message.message_data == null)
				return null;
			
			// Data
			NBTTagCompound data = message.message_data;
			
			com.abnull.yrs.YRSMod.debug.print_debug("ClientDataMessage", 61, data.toString());
			
			if(data.hasKey("start_data_name"))
			{
				big_data_name = data.getString("start_data_name");
				is_big_data_loading = true;
			}
			else if(data.hasKey("end_data_name"))
			{
				big_data_name = "";
				is_big_data_loading = false;
				
			}
			else
			{				
				if(is_big_data_loading)
				{
					if(big_data_name.equals("skill_texture"))
					{
						String skill_texture_data_name = data.getString("name");
						com.abnull.yrs.proxy.ClientProxy.skill_texture_data.setTag(skill_texture_data_name, data);
					}
					else if(big_data_name.equals("background_sound"))
					{
						String sound_name = data.getString("name");
						byte[] bytes = data.getByteArray(sound_name);
						com.abnull.yrs.proxy.ClientProxy.sound_data_map.put(sound_name, bytes);
					}
				}
				else
				{
					if(data.getBoolean("is_init"))
					{
						com.abnull.yrs.proxy.ClientProxy.player_data = data;
						
						String[] job_name_array = data.getString("jobs").split(";");
						String[] skill_name_array = data.getString("skill_array").split(",");
						for(int n = 0; n < job_name_array.length; n++)
						{
							String job_name = job_name_array[n];
							NBTTagCompound job_data = data.getCompoundTag("job_data").getCompoundTag(job_name);
							
							for(int m = 0; m < skill_name_array.length; m++)
							{
								String skill_name = skill_name_array[m];
								if(!skill_name.equals("") && job_data.getCompoundTag("skill_data").hasKey(skill_name))
								{
									NBTTagCompound skill_data = job_data.getCompoundTag("skill_data").getCompoundTag(skill_name);
									com.abnull.yrs.proxy.ClientProxy.skill_data_map.put(skill_name, new com.abnull.yrs.util.SkillData(skill_data, Integer.parseInt(data.getCompoundTag("skill_level_data").getString(skill_name))));
									com.abnull.yrs.proxy.ClientProxy.skill_cooltime_map.put(skill_name, 0L);
								}
							}
						}
						
						NBTTagCompound skill_slot_data = data.getCompoundTag("skill_slot");
						com.abnull.yrs.customgui.CustomGuiSkillTree.skill_slot_array = new com.abnull.yrs.customgui.CustomGuiSkillSlot[6];
						for(int n = 0; n < 6; n++)
						{
							String skill_name = skill_slot_data.getString("slot" + (n + 1));
							com.abnull.yrs.customgui.CustomGuiSkillTree.skill_slot_array[n] = new com.abnull.yrs.customgui.CustomGuiSkillSlot(0, 0, 16, skill_name);
						}
					}
					else
					{
						String[] label_array = data.getString("label_array").split(",");
						if(data.getBoolean("is_string_data"))
						{
							for(int n = 0; n < label_array.length; n++)
							{
								String label = label_array[n];
								String contents = data.getString(label);
								
								com.abnull.yrs.proxy.ClientProxy.player_data.setString(label, contents);
								
								if(label.equals("max-hp"))
									PlayerBasicDataManager.set_max_health(((EntityPlayer)net.minecraft.client.Minecraft.getMinecraft().thePlayer), Float.parseFloat(contents));
								else if(label.equals("max-mp"))
									PlayerBasicDataManager.set_max_mana(com.abnull.yrs.proxy.ClientProxy.player_data, Integer.parseInt(contents));
								else if(label.equals("region"))
									MinecraftForge.EVENT_BUS.post(new com.abnull.yrs.event.client.player.PlayerEnterRegionEvent(net.minecraft.client.Minecraft.getMinecraft().thePlayer, contents));
								// ((CustomGuiIngame) ClientProxy.render_gui_screen_event_handler.get_custom_gui_ingame()).in_region_name_drawing(contents, 5.0f);
								///////////////////////////////////////////////////////////// This Point
							}
						}
						else
						{
							for(int n = 0; n < label_array.length; n++)
							{
								String label = label_array[n];
								NBTTagCompound contents = data.getCompoundTag(label);
								
								com.abnull.yrs.proxy.ClientProxy.player_data.setTag(label, contents);
								
								if(label.equals("skill_data"))
								{
									String[] job_name_array = data.getString("jobs").split(";");
									String[] skill_name_array = data.getString("skill_array").split(",");
									for(int m = 0; m < job_name_array.length; m++)
									{
										String job_name = job_name_array[m];
										NBTTagCompound job_data = data.getCompoundTag("job_data").getCompoundTag(job_name);
										
										for(int l = 0; l < skill_name_array.length; l++)
										{
											String skill_name = skill_name_array[l];
											NBTTagCompound skill_data = job_data.getCompoundTag("skill_data").getCompoundTag(skill_name);
											com.abnull.yrs.proxy.ClientProxy.skill_data_map.put(skill_name, new com.abnull.yrs.util.SkillData(skill_data, Integer.parseInt(data.getCompoundTag("skill_level_data").getString(skill_name))));
											com.abnull.yrs.proxy.ClientProxy.skill_cooltime_map.put(skill_name, 0L);
										}
									}
								}
							}
						}
					}
				}
			}
			
			return null;
		}
	}
}
