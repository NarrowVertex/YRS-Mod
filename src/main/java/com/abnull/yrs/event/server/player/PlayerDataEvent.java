package com.abnull.yrs.event.server.player;

import com.abnull.yrs.player.data.PlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class PlayerDataEvent extends PlayerEvent {

	public PlayerData player_data;
	
	public PlayerDataEvent(EntityPlayer player, PlayerData data)
	{
		super(player);
		player_data = data;
	}
	
}
