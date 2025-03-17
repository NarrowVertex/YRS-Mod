package com.abnull.yrs.item.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemYRSHealthPotion extends ItemYRSPotion {

	public ItemYRSHealthPotion(String name, int amount)
	{
		super(name, 64, amount);
	}
	
	public boolean onItemUsed(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
	{
		if((int)p_77659_3_.getHealth() != (int)p_77659_3_.getMaxHealth())
		{
			float health = p_77659_3_.getHealth();
			
			if((health + this.charging_amount) > p_77659_3_.getMaxHealth())
			{
				p_77659_3_.setHealth(p_77659_3_.getMaxHealth());
			}
			else
			{
				p_77659_3_.setHealth((health + this.charging_amount));
			}
			p_77659_1_.stackSize -= 1;
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("yrs:Health Potion");
	}
}
