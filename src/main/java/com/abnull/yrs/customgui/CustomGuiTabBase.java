package com.abnull.yrs.customgui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiTabBase extends Gui {

	ResourceLocation gui_resource_location;
	
	public ArrayList<Tab> tab_list = new ArrayList<Tab>();
	public Tab last_pressed_tab;
	
	public int start_x;
	public int start_y;
	
	// Without width, height
	public int add_x;
	public int add_y;
	
	public int not_pressed_texture_u;
	public int not_pressed_texture_v;
	
	public int pressed_texture_u;
	public int pressed_texture_v;
	
	public int each_width;
	public int each_height;
	
	// NBTTagCompound nbt_array_data : { count : (int), 0 : (NBTTagCompound), 1 : (NBTTagCompound), - - - }
	public CustomGuiTabBase(int s_x, int s_y, int a_x, int a_y, int np_u, int np_v, int p_u, int p_v, int e_w, int e_h, NBTTagCompound nbt_array_data, ResourceLocation r)
	{
		this.start_x = s_x;
		this.start_y = s_y;
		
		this.add_x = a_x;
		this.add_y = a_y;
		
		this.not_pressed_texture_u = np_u;
		this.not_pressed_texture_v = np_v;
		
		this.pressed_texture_u = p_u;
		this.pressed_texture_v = p_v;
		
		this.each_width = e_w;
		this.each_height = e_h;
		
		this.gui_resource_location = r;
		
		int nbt_array_data_count = nbt_array_data.getInteger("count");
		for(int n = 0; n < nbt_array_data_count; n++)
		{
			NBTTagCompound data = nbt_array_data.getCompoundTag("" + n);
			tab_list.add(new Tab(n, data,
					start_x + (add_x * n),
					start_y + (add_y * n),
					each_width,
					each_height));
		}
		tab_list.get(0).press();
		last_pressed_tab = tab_list.get(0);
	}
	
	public void draw()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(gui_resource_location);
		
		for(int n = 0; n < tab_list.size(); n++)
		{
			Tab tab = tab_list.get(n);
			boolean is_pressed = tab.get_is_pressed();
			tab.draw(
					is_pressed ? pressed_texture_u : not_pressed_texture_u, 
					is_pressed ? pressed_texture_v : not_pressed_texture_v);
		}
	}
	
	public boolean mounse_press(int x, int y)
	{
		for(int n = 0; n < tab_list.size(); n++)
		{
			Tab tab = tab_list.get(n);
			int tab_s_x = tab.x;
			int tab_s_y = tab.y;
			int tab_e_x = tab.x + tab.w;
			int tab_e_y = tab.y + tab.h;
			
			if(tab_s_x <= x && x <= tab_e_x)
			{
				if(tab_s_y <= y && y <= tab_e_y)
				{
					for(int n1 = 0; n1 < tab_list.size(); n1++)
					{
						Tab tab1 = tab_list.get(n1);
						tab1.is_pressed = false;
					}
					tab.press();
					last_pressed_tab = tab;
					
    				Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
					
					return true;
				}
			}
		}
		return false;
	}
	
	public Tab get_last_pressed_tab()
	{
		return this.last_pressed_tab;
	}
	
	protected class Tab extends Gui
	{
		int tab_number;
		
		NBTTagCompound tab_data;
		boolean is_pressed = false;
		
		int x, y, w, h;
		
		public Tab(int n, NBTTagCompound data, int x, int y, int w, int h)
		{
			tab_number = n;
			tab_data = data;
			
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		
		public void press()
		{
			is_pressed = true;
		}
		
		public boolean get_is_pressed()
		{
			return this.is_pressed;
		}
		
		public NBTTagCompound get_tab_data()
		{
			return this.tab_data;
		}
		
		public void draw(int u, int v)
		{
			this.drawTexturedModalRect(x, y, u, v, w, h);
		}
	}
}
