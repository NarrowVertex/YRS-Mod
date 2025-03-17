package com.abnull.yrs.item.potion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemYRSPotion extends Item {

	protected int stack_max_size = 0;
	protected int charging_amount = 0;
	
	public ItemYRSPotion(String name, int stack_max_size, int charging_amount)
	{
		setUnlocalizedName(name);
		setTextureName(name);
		
		this.stack_max_size = stack_max_size;
		setMaxStackSize(stack_max_size);
		
		this.charging_amount = charging_amount;
		
		setCreativeTab(CreativeTabs.tabBrewing);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return stack_max_size;
	}
	
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
		onItemUsed(p_77659_1_, p_77659_2_, p_77659_3_);
		return p_77659_1_;
    }
	
	public abstract boolean onItemUsed(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_);
}
