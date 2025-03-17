package com.abnull.yrs.customgui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.abnull.yrs.proxy.ClientProxy;
import com.abnull.yrs.util.SkillData;
import com.abnull.yrs.util.SkillTextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiSkillPanel extends CustomGuiBase {
	
	public String skill_name;
	public SkillData skill_data;
	
	int gui_x;
	int gui_y;
	
	int skill_texture_x;
	int skill_texture_y;
	int skill_texture_w;
	int skill_texture_h;
	
	public CustomGuiSkillUpgradeButton skill_upgrade_button;
	
	public CustomGuiSkillPanel(String s_n, SkillData s_d, int x, int y)
	{
		super(Minecraft.getMinecraft());
		
		skill_name = s_n;
		skill_data = s_d;
		
		gui_x = x;
		gui_y = y;
		
		skill_upgrade_button = new CustomGuiSkillUpgradeButton(1);
	}
	
	public boolean is_under_mouse_cursor(int x, int y)
	{
		if(!skill_data.has_skill_permission())
		{
			if(skill_data.get_skill_permission().equals("need_activation"))
			
			return false;
		}
		
		if(skill_texture_x <= x && x <= (skill_texture_x + skill_texture_w))
		{
			if(skill_texture_y <= y && y <= (skill_texture_y + skill_texture_h))
			{
				return true;
			}
		}
		
		return false;
	}

	public void draw(int bar_level)
	{		
		draw_background(gui_x, gui_y + (bar_level * 31));
		
		skill_texture_x = gui_x + 7;
		skill_texture_y = gui_y + (bar_level * 31) + 7;
		skill_texture_w = 16;
		skill_texture_h = 16;
		draw_skill_texture(skill_texture_x, skill_texture_y, skill_name);
		
		draw_skill_name(gui_x + 7 + 22, gui_y + (bar_level * 31) + 7, skill_name);
		draw_skill_description(gui_x + 7 + 22, gui_y + (bar_level * 31) + 7 + 10);
		draw_skill_upgrade_button(gui_x + 94, gui_y + (bar_level * 31) + 7);
	}
	
	private void draw_background(int x, int y)
	{
		this.mc.getTextureManager().bindTexture(CustomGuiSkillTree.skill_tree_texture);
		this.drawTexturedModalRect(x, y, 0, 190, 111, 31);
	}
	
	private void draw_skill_texture(int x, int y, String skill_name)
	{
		SkillTextureHelper.draw_skill_texture(skill_name, x, y, this.zLevel, this.skill_texture_w, this.skill_texture_h);
		this.mc.getTextureManager().bindTexture(CustomGuiSkillTree.skill_tree_texture);
		this.drawTexturedModalRect(x - 2, y - 2, 0, 222, this.skill_texture_w + 4, this.skill_texture_h + 4);
		
		if(!skill_data.has_skill_permission())
		{
			if(skill_data.get_skill_permission().equals("need_activation"))
			{
				draw_skill_nonactivation_texture(x, y);
			}
			else
			{
				draw_skill_locking_texture(x, y);
			}
		}
	}
	
	private void draw_skill_name(int x, int y, String skill_name)
	{
		this.drawString(this.mc.fontRenderer, skill_name, x, y, 0xCED261, true, 1f);
	}
	
	private void draw_skill_cooltime(int x, int y)
	{
		float cooltime = skill_data.cooltime;
		this.drawString(this.mc.fontRenderer, "" + cooltime, x, y, 0xFFFFFF, true, 1f);
	}
	
	private void draw_skill_description(int x, int y)
	{
		NBTTagCompound player_data = ClientProxy.player_data;
		
		String skill_permission = skill_data.get_skill_permission();
		if(!skill_permission.equals("none"))
		{
			if(skill_permission.equals("need_activation"))
			{
				draw_need_activation(x, y);
				return;
			}
			else if(skill_permission.equals("need_stat"))
			{
				NBTTagCompound skill_need_stat_data = skill_data.need_stat_data;
				String[] need_stat_array = new String[skill_need_stat_data.getInteger("tag_contents_count")];
				for(int m = 0; m < need_stat_array.length; m++)
				{
					need_stat_array[m] = skill_need_stat_data.getString("" + m);
				}
				draw_need_stat(x, y, need_stat_array);
				return;
			}
			else if(skill_permission.equals("need_skill"))
			{
				NBTTagCompound skill_need_skill_data = skill_data.need_skill_data;
				String[] need_skill_array = new String[skill_need_skill_data.getInteger("tag_contents_count")];
				for(int m = 0; m < need_skill_array.length; m++)
				{
					need_skill_array[m] = skill_need_skill_data.getString("" + m);
				}
				draw_need_skill(x, y, need_skill_array);
				return;
			}
			else if(skill_permission.equals("need_player_level"))
			{
				int need_player_level = skill_data.need_player_level;
				draw_need_player_level(x, y, need_player_level);
				return;
			}
		}
		
		String description = skill_data.description;
		this.drawString(this.mc.fontRenderer, description, x, y, 0xFFFFFF, true, 1f);
	}
	
	private void draw_need_activation(int x, int y)
	{
		String description = "";
		description = "Need Skill Activation";
		this.drawString(this.mc.fontRenderer, description, x, y, 0xC20E0E, true, 1f);
	}
	
	private void draw_need_stat(int x, int y, String[] need_stat_array)
	{
		String description = "";
		for(int n = 0; n < need_stat_array.length; n++)
		{
			description += need_stat_array[n];
			if(n != need_stat_array.length - 1)
				description += ",";
		}
		this.drawString(this.mc.fontRenderer, "Need " + description, x, y, 0xC20E0E, true, 1f);
	}
	
	private void draw_need_skill(int x, int y, String[] need_skill_array)
	{
		String description = "";
		for(int n = 0; n < need_skill_array.length; n++)
		{
			description += need_skill_array[n];
			if(n != need_skill_array.length - 1)
				description += ",";
		}
		this.drawString(this.mc.fontRenderer, "Need " + description, x, y, 0xC20E0E, true, 1f);
	}
	
	private void draw_need_player_level(int x, int y, int need_player_level)
	{
		this.drawString(this.mc.fontRenderer, "Need Lv." + need_player_level, x, y, 0xC20E0E, true, 1f);
	}
	
	private void draw_skill_nonactivation_texture(int x, int y)
	{
		this.mc.getTextureManager().bindTexture(CustomGuiSkillTree.skill_tree_texture);
		this.drawTexturedModalRect(x, y, 170, 50, skill_texture_w, skill_texture_h);
	}
	
	private void draw_skill_locking_texture(int x, int y)
	{
		this.mc.getTextureManager().bindTexture(CustomGuiSkillTree.skill_tree_texture);
		this.drawTexturedModalRect(x, y, 170, 50, skill_texture_w, skill_texture_h);
		this.drawTexturedModalRect(x, y, 186, 50, skill_texture_w, skill_texture_h);
	}
	
	private void draw_skill_upgrade_button(int x, int y)
	{
		skill_upgrade_button.drawButton(this.mc, x, y, x, y, skill_data.has_skill_upgrading_permission());
	}
}
