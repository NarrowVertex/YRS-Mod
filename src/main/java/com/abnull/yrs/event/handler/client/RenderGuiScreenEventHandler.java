package com.abnull.yrs.event.handler.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.abnull.yrs.customgui.CustomGuiBase;
import com.abnull.yrs.customgui.CustomGuiIngame;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class RenderGuiScreenEventHandler {

	private CustomGuiIngame custom_gui_ingame;
	
	boolean is_initalized = false;
	
	public RenderGuiScreenEventHandler()
	{
		
	}
	
	public void init()
	{
		custom_gui_ingame = new CustomGuiIngame(Minecraft.getMinecraft());
	}
	
	@SubscribeEvent
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event)
    {
		if(is_initalized == false)
		{
			init();
			is_initalized = true;
		}
		
		ElementType gui_type = event.type;
		
		if(gui_type == ElementType.HOTBAR || gui_type == ElementType.HEALTH || gui_type == ElementType.FOOD || gui_type == ElementType.EXPERIENCE || gui_type == ElementType.ARMOR || gui_type == ElementType.AIR || gui_type == ElementType.HEALTHMOUNT)
		{
			event.setCanceled(true);
		}
		
		if (gui_type != ElementType.HOTBAR) 
		{
			return;
		}
		
		custom_gui_ingame.draw(event.partialTicks);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
    }
	
	public CustomGuiBase get_custom_gui_ingame() 
	{
		return this.custom_gui_ingame;
	}
}
