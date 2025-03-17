package com.abnull.yrs.event.server.player;

import com.abnull.yrs.player.data.PlayerData;
import com.abnull.yrs.region.Region;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerExitRegionEvent extends PlayerDataEvent {

	public Region region;
	
	public PlayerExitRegionEvent(EntityPlayer player, PlayerData data, Region region)
	{
		super(player, data);
		this.region = region;
	}
	
}
