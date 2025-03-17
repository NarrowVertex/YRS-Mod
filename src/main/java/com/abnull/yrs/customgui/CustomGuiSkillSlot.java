package com.abnull.yrs.customgui;

import com.abnull.yrs.util.SkillTextureHelper;

import net.minecraft.client.Minecraft;

public class CustomGuiSkillSlot extends CustomGuiBase {

	private int gui_x;
	private int gui_y;
	
	private int size;
	
	private String skill_name;
	
	public CustomGuiSkillSlot(int x, int y, int s, String s_n)
	{
		super(Minecraft.getMinecraft());
		
		this.gui_x = x;
		this.gui_y = y;
		
		this.size = s;
		
		this.skill_name = s_n;
	}
	
	public void draw()
	{
		if(!this.skill_name.equals("n"))
		{
			SkillTextureHelper.draw_skill_texture(this.skill_name, this.gui_x, this.gui_y, this.zLevel, this.size, this.size);
		}
	}
	
	public void set_skill_name(String name)
	{
		this.skill_name = name;
	}
	
	public String get_skill_name()
	{
		return this.skill_name;
	}
	
	public boolean is_skill_exist()
	{
		return !this.skill_name.equals("n");
	}
	
	public void set_skill_none()
	{
		this.skill_name = "n";
	}
	
	public boolean onMouseClicked(int x, int y)
	{
		if(this.gui_x <= x && x <= (this.gui_x + this.size))
		{
			if(this.gui_y <= y && y <= (this.gui_y + this.size))
			{
				return true;
			}
		}
		
		return false;
	}
}
