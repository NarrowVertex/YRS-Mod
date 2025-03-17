package com.abnull.yrs.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

	public void pre_init(FMLPreInitializationEvent event);
	
	public void init(FMLInitializationEvent event);
	
	public void post_init(FMLPostInitializationEvent event);
}
