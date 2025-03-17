package com.abnull.yrs.event.handler.client;

import java.util.Iterator;

import com.abnull.yrs.customgui.CustomGuiIngame;
import com.abnull.yrs.event.client.player.PlayerEnterRegionEvent;
import com.abnull.yrs.proxy.ClientProxy;
import com.abnull.yrs.util.SoundHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ThePlayerEventHandler {

	boolean is_potion_inited = false;
	
	public ThePlayerEventHandler()
	{
		
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.RenderTickEvent event) 
	{
		if(is_potion_inited == false) 
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if(player != null)
			{
				for(Iterator i = player.getActivePotionEffects().iterator(); i.hasNext();)
				{
					PotionEffect pe = (PotionEffect) i.next();
					ClientProxy.granted_effect_duration_map.put(pe.getPotionID(), pe.getDuration());
				}
				is_potion_inited = true;
			}
		}
		else
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if(player != null)
			{
				for(Iterator i = player.getActivePotionEffects().iterator(); i.hasNext();)
				{
					PotionEffect pe = (PotionEffect) i.next();
					if(!ClientProxy.granted_effect_duration_map.containsKey(pe.getPotionID()))
					{
						ClientProxy.granted_effect_duration_map.put(pe.getPotionID(), pe.getDuration());
					}
					else if(ClientProxy.granted_effect_duration_map.get(pe.getPotionID()) < pe.getDuration())
					{
						ClientProxy.granted_effect_duration_map.remove(pe.getPotionID());
						ClientProxy.granted_effect_duration_map.put(pe.getPotionID(), pe.getDuration());
					}
				}
				
				for (Iterator i = ClientProxy.granted_effect_duration_map.keySet().iterator(); i.hasNext();)
				{
					int pe_id = (Integer)i.next();
				    if(!player.isPotionActive(pe_id))
				    	i.remove();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerEnterRegion(PlayerEnterRegionEvent event)
	{
		CustomGuiIngame cgi = (CustomGuiIngame) ClientProxy.render_gui_screen_event_handler.get_custom_gui_ingame();
		cgi.enter_in_region(event.region_name);
		SoundHelper.play_sound(event.region_name);
	}
}
