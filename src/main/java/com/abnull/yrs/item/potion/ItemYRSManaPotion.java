package com.abnull.yrs.item.potion;

import com.abnull.yrs.proxy.ServerProxy;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemYRSManaPotion extends ItemYRSPotion {

	public ItemYRSManaPotion(String name, int amount)
	{
		super(name, 64, amount);
	}
	
	public boolean onItemUsed(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			NBTTagCompound player_data = ServerProxy.player_data_manager.get_player_data(p_77659_3_).data;
			
			int player_max_mp = Integer.parseInt(player_data.getString("max-mp"));
			int player_mp = Integer.parseInt(player_data.getString("mp"));
			
			if(player_mp != player_max_mp)
			{
				if((player_mp + this.charging_amount) > player_max_mp)
				{
					player_data.setString("mp", "" + player_max_mp);
				}
				else
				{
					player_data.setString("mp", "" + (player_mp + this.charging_amount));
				}
				p_77659_1_.stackSize -= 1;
				return true;
			}
			ServerProxy.sending_data_manager.send_each_data_to_player_string(p_77659_3_, player_data, "mp");
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("yrs:Mana Potion");
	}
	
}
