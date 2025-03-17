package com.abnull.yrs.event.handler.client;

import org.lwjgl.input.Keyboard;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.customgui.CustomGuiSkillTree;
import com.abnull.yrs.customgui.CustomGuiStat;
import com.abnull.yrs.customgui.handler.CustomGuiHandler;
import com.abnull.yrs.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GuiKeyInputEventHandler {

	public static final KeyBinding stat_gui_key = new KeyBinding("Stat Gui", Keyboard.KEY_I, "YRS Mod - GuiKey");
	public static final KeyBinding skill_tree_gui_key = new KeyBinding("Skill Tree Gui", Keyboard.KEY_K, "YRS Mod - GuiKey");
	public static final KeyBinding potion_tree_gui_key = new KeyBinding("Potion Tree Gui", Keyboard.KEY_J, "YRS Mod - GuiKey");
	
	public GuiKeyInputEventHandler()
	{
		ClientRegistry.registerKeyBinding(stat_gui_key);
		ClientRegistry.registerKeyBinding(skill_tree_gui_key);
		ClientRegistry.registerKeyBinding(potion_tree_gui_key);
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		if(stat_gui_key.getIsKeyPressed())
		{
			Minecraft.getMinecraft().displayGuiScreen(new CustomGuiStat());
		}
		
		if(skill_tree_gui_key.getIsKeyPressed())
		{
			Minecraft.getMinecraft().displayGuiScreen(new CustomGuiSkillTree());
		}
		
		if(potion_tree_gui_key.getIsKeyPressed())
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			player.openGui(YRSMod.instance, CustomGuiHandler.POTION_SLOT_TILE_ENTITY_GUI, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
			ClientProxy.client_sending_data_manager.send_data_to_server("open_gui_screen", new String[][]{ { "gui_screen_name", "potion tree"} });
		}
	}
}
