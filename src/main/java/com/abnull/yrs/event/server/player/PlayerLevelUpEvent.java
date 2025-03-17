package com.abnull.yrs.event.server.player;

import com.abnull.yrs.player.data.PlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerLevelUpEvent extends PlayerDataEvent {

	public int pre_level;
	public int level;
	
	public PlayerLevelUpEvent(EntityPlayer player, PlayerData data, int pre_level, int level)
	{
		super(player, data);
		this.level = level;
		this.pre_level = pre_level;
	}
}
