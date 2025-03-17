package com.abnull.yrs.event.client.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerEnterRegionEvent extends PlayerEvent {

	public String region_name;
	
	public PlayerEnterRegionEvent(EntityPlayer player, String name)
	{
		super(player);
		region_name = name;
	}
}
