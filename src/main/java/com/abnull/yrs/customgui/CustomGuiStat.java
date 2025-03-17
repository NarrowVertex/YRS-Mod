package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import com.abnull.yrs.proxy.ClientProxy;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiStat extends CustomGuiScreenBase {

	public static final ResourceLocation stat_resource_location = new ResourceLocation("yrs", "textures/gui/stat_gui.png");
	
	int gui_x = 0;
	int gui_y = 0;
	
	public void initGui()
	{
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		gui_x = sr.getScaledWidth() / 2 - 84;
		gui_y = sr.getScaledHeight() / 2 - 54;
		
		this.buttonList.clear();
		this.buttonList.add(new CutomGuiStatUpgradeButton(0, gui_x  + 73, gui_y + 26 + (21 * 0)));
		this.buttonList.add(new CutomGuiStatUpgradeButton(1, gui_x  + 73, gui_y + 26 + (21 * 1)));
		this.buttonList.add(new CutomGuiStatUpgradeButton(2, gui_x  + 73, gui_y + 26 + (21 * 2)));
		this.buttonList.add(new CutomGuiStatUpgradeButton(3, gui_x  + 73, gui_y + 26 + (21 * 3)));
	}
	
	protected void actionPerformed(GuiButton button)
	{
		NBTTagCompound data = ClientProxy.player_data;
		if(!data.hasKey("stat-point"))
			return;
		
		if(!(Integer.parseInt(data.getString("stat-point")) > 0))
			return;
		
		switch(button.id)
		{
		case 0:
			ClientProxy.client_sending_data_manager.send_data_to_server("upgrade_stat", new String[][]{ 
					{"name" , "str"} });
			break;
		case 1:
			ClientProxy.client_sending_data_manager.send_data_to_server("upgrade_stat", new String[][]{ 
					{"name" , "def"} });
			break;
		case 2:
			ClientProxy.client_sending_data_manager.send_data_to_server("upgrade_stat", new String[][]{ 
					{"name" , "acc"} });
			break;
		case 3:
			ClientProxy.client_sending_data_manager.send_data_to_server("upgrade_stat", new String[][]{ 
					{"name" , "agi"} });
			break;
		}
	}
	
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		gui_x = sr.getScaledWidth() / 2 - 84;
		gui_y = sr.getScaledHeight() / 2 - 54;
		
		this.mc.getTextureManager().bindTexture(stat_resource_location);
		this.drawTexturedModalRect(gui_x, gui_y, 0, 0, 168, 108);
		
		NBTTagCompound data = ClientProxy.player_data;
		NBTTagCompound stat_data = data.getCompoundTag("stat");
		
		int str_point = Integer.parseInt(stat_data.getString("str"));
		int def_point = Integer.parseInt(stat_data.getString("def"));
		int acc_point = Integer.parseInt(stat_data.getString("acc"));
		int agi_point = Integer.parseInt(stat_data.getString("agi"));
		
		this.mc.fontRenderer.drawString("Stat Point : " + data.getString("stat-point"), gui_x + 6, gui_y + 6, 0xFFFFFF, false);
		
		this.mc.fontRenderer.drawString("STR : " + str_point, gui_x + 6, gui_y + 26 + (21 * 0), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("DEF : " + def_point, gui_x + 6, gui_y + 26 + (21 * 1), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("ACC : " + acc_point, gui_x + 6, gui_y + 26 + (21 * 2), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("AGI : " + agi_point, gui_x + 6, gui_y + 26 + (21 * 3), 0xFFFFFF, false);
		/*
		NBTTagCompound stat_setting_data = data.getCompoundTag("stat-data");
		float str_strength = stat_setting_data.getFloat("str-strength");
		float def_defence = stat_setting_data.getFloat("def-defence");
		float dex_probability = stat_setting_data.getFloat("dex-probability");
		float dex_critical_multiple = stat_setting_data.getFloat("dex-critical-multiple");
		float agi_probability = stat_setting_data.getFloat("agi-probability");
		
		this.mc.fontRenderer.drawString("Strength : " + str_strength * str_point, gui_x + 96, gui_y + 26 + (21 * 0), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("Defence : " + def_defence * 100 * def_point + "%", gui_x + 96, gui_y + 26 + (21 * 1), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("Critical Prob. : " + dex_probability * 100 * str_point + "%", gui_x + 96, gui_y + 26 + (21 * 2), 0xFFFFFF, false);
		this.mc.fontRenderer.drawString("Agility Prob. : " + agi_probability * 100 * agi_point + "%", gui_x + 96, gui_y + 26 + (21 * 3), 0xFFFFFF, false);
		*/
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}
