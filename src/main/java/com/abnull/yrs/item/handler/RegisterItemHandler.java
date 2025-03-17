package com.abnull.yrs.item.handler;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.item.potion.ItemYRSHealthPotion;
import com.abnull.yrs.item.potion.ItemYRSManaPotion;

import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterItemHandler {

	public RegisterItemHandler()
	{
		
	}
	
	public void register()
	{
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_F", 30), "HealthPotion_F", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_E", 70), "HealthPotion_E", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_D", 150), "HealthPotion_D", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_C", 300), "HealthPotion_C", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_B", 750), "HealthPotion_B", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_A", 1500), "HealthPotion_A", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_S", 3000), "HealthPotion_S", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_SS", 7500), "HealthPotion_SS", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSHealthPotion("HealthPotion_SSS", 15000), "HealthPotion_SSS", YRSMod.MODID);
		
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_F", 30), "ManaPotion_F", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_E", 70), "ManaPotion_E", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_D", 150), "ManaPotion_D", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_C", 300), "ManaPotion_C", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_B", 750), "ManaPotion_B", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_A", 1500), "ManaPotion_A", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_S", 3000), "ManaPotion_S", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_SS", 7500), "ManaPotion_SS", YRSMod.MODID);
		GameRegistry.registerItem(new ItemYRSManaPotion("ManaPotion_SSS", 15000), "ManaPotion_SSS", YRSMod.MODID);
	}
	
}
