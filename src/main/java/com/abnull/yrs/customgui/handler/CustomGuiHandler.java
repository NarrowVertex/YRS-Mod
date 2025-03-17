package com.abnull.yrs.customgui.handler;

import com.abnull.yrs.tileentity.TileEntityPotionTree;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CustomGuiHandler implements IGuiHandler {

	public static final int POTION_SLOT_TILE_ENTITY_GUI = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == POTION_SLOT_TILE_ENTITY_GUI)
			return new com.abnull.yrs.container.ContainerPotionTree(player.inventory, new TileEntityPotionTree(player));
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == POTION_SLOT_TILE_ENTITY_GUI)
			return new com.abnull.yrs.customgui.CustomGuiPotionTree(player.inventory, new TileEntityPotionTree(player));
		
		return null;
	}
}
