package com.abnull.yrs.customgui;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.abnull.yrs.proxy.ClientProxy;
import com.abnull.yrs.util.SkillData;
import com.abnull.yrs.util.SkillTextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiSkillTree extends CustomGuiScreenBase {

	public static final ResourceLocation skill_tree_texture = new ResourceLocation("yrs", "textures/gui/skill_tree_gui.png");
	public static final ResourceLocation skill_information_texture = new ResourceLocation("yrs", "textures/gui/skill_information_gui.png");
	
	private NBTTagCompound player_data;
	
	int gui_x = 0;
	int gui_y = 0;
	
	int bar_level = 0;
	
	boolean is_dragging = false;
	String dragging_skill_name = "";
	
	public CustomGuiSkillTreeJobTab job_tab_gui;
	public NBTTagCompound last_job_data;
	public CustomGuiSkillPanel[] skill_panel_array;
	public static CustomGuiSkillSlot[] skill_slot_array = new CustomGuiSkillSlot[6];
	
	public CustomGuiSkillTree()
	{
		player_data = ClientProxy.player_data;
	}
	
	public void initGui()
	{
		ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int width = scaledresolution.getScaledWidth();
		int height = scaledresolution.getScaledHeight();
		
		gui_x = (width / 2) - (150 / 2);
		gui_y = (height / 2) - (190 / 2);
		
		this.buttonList.clear();
		
		NBTTagCompound player_data = ClientProxy.player_data;
		
		set_job_tab(gui_x, gui_y);
		set_skill_panel_array(last_job_data, gui_x, gui_y);
		set_skill_slot_array(player_data, gui_x, gui_y);
	}
	
	
	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
		
		int wheel = Mouse.getDWheel();
		if(wheel != 0)
			mouse_wheel((-wheel / 120));
		
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mode)
	{
		super.mouseClicked(x, y, mode);
		
		if (mode == 0)
        {
			if(job_tab_gui.mounse_press(x, y))
			{
				last_job_data = job_tab_gui.last_pressed_tab.get_tab_data();
				set_skill_panel_array(last_job_data, gui_x, gui_y);
				bar_level = 0;
				return;
			}
			
			for(int n = 0; n < 4; n++)
	    	{
	    		if(!(bar_level + n >= skill_panel_array.length))
	    		{
	    			CustomGuiSkillPanel skill_panel = skill_panel_array[bar_level + n];
	    			if(skill_panel != null)
	    			{
	    				if(skill_panel.is_under_mouse_cursor(x, y))
			    		{
			    			String skill_name = skill_panel.skill_name;
			    			
			    			dragging_skill_name = skill_name;
			    			is_dragging = true;
			    			return;
			    		}
			    		if(skill_panel.skill_upgrade_button.mousePressed(this.mc, x, y))
			    		{
			    			if(skill_panel.skill_data.has_skill_upgrading_permission())
			    			{
			    				String skill_name = skill_panel.skill_name;
				    			ClientProxy.client_sending_data_manager.send_data_to_server("upgrade_skill_level", new String[][]{ { "skill_name", skill_name } });
			    			}
			    		}
	    			}
	    		}
	    	}
			
			if(is_dragging == true)
			{
				for(int n = 0; n < skill_slot_array.length; n++)
				{
					CustomGuiSkillSlot skill_slot = skill_slot_array[n];
					if(skill_slot.onMouseClicked(x, y))
					{
						for(int m = 0; m < 6; m++)
						{
							if(m != n)
							{
								CustomGuiSkillSlot another_skill_slot = skill_slot_array[m];
								if(dragging_skill_name.equals(another_skill_slot.get_skill_name()))
									return;
							}
						}
						skill_slot.set_skill_name(dragging_skill_name);
						is_dragging = false;
						ClientProxy.client_sending_data_manager.send_data_to_server("set_skill_slot", new String[][]{ { "slot_number", "" + n }, { "skill_name", skill_slot.get_skill_name() } });
						return;
					}
				}
			}
			else
			{
				for(int n = 0; n < skill_slot_array.length; n++)
				{
					CustomGuiSkillSlot skill_slot = skill_slot_array[n];
					if(skill_slot.is_skill_exist())
					{
						if(skill_slot.onMouseClicked(x, y))
						{
							dragging_skill_name = skill_slot.get_skill_name();
							is_dragging = true;
							
							skill_slot.set_skill_none();
							ClientProxy.client_sending_data_manager.send_data_to_server("set_skill_slot", new String[][]{ { "slot_number", "" + n }, { "skill_name", skill_slot.get_skill_name() } });
							return;
						}
					}
				}
			}
        }
		
		if(is_dragging == true)
		{
			is_dragging = false;
			dragging_skill_name = "";
		}
	}
	
	private void mouse_wheel(int wheel)
	{
		if(this.skill_panel_array.length > 4)
    		this.bar_level += wheel;
    	
		if(this.bar_level > (this.skill_panel_array.length - 4))
			this.bar_level = (this.skill_panel_array.length - 4);
		if(this.bar_level < 0)
			this.bar_level = 0;
	}
	
	
	private void set_job_tab(int x, int y)
	{
		String[] player_job_name_array = player_data.getString("jobs").split(";");
		NBTTagCompound job_data_tag = player_data.getCompoundTag("job_data");
		NBTTagCompound data_tag = new NBTTagCompound();
		int n;
		for(n = 0; n < player_job_name_array.length; n++)
		{
			String job_name = player_job_name_array[n];
			NBTTagCompound job_data = job_data_tag.getCompoundTag(job_name);
			data_tag.setTag("" + n, job_data);
		}
		data_tag.setInteger("count", n);
		job_tab_gui = new CustomGuiSkillTreeJobTab(x, y, data_tag);
		last_job_data = job_tab_gui.get_last_pressed_tab().get_tab_data();
	}
	
	private void set_skill_panel_array(NBTTagCompound job_data, int x, int y)
	{
		String[] skill_name_array = job_data.getString("skill").split(",");
		skill_panel_array = new CustomGuiSkillPanel[skill_name_array.length];

		for(int m = 0; m < skill_name_array.length; m++)
		{
			String skill_name = skill_name_array[m];
			if(!skill_name.equals(""))
			{
				SkillData skill_data = ClientProxy.skill_data_map.get(skill_name);
				if(skill_data != null)
				{
					skill_panel_array[m] = new CustomGuiSkillPanel(skill_name, skill_data, x + 9, y + 24);
				}
			}
		}
	}
	
	private void set_skill_slot_array(NBTTagCompound player_data, int x, int y)
	{
		NBTTagCompound skill_slot_data = player_data.getCompoundTag("skill_slot");
		skill_slot_array = new CustomGuiSkillSlot[6];
		for(int n = 0; n < 6; n++)
		{
			String skill_name = skill_slot_data.getString("slot" + (n + 1));
			skill_slot_array[n] = new CustomGuiSkillSlot(x + 24 + 18 * n, y + 160, 16, skill_name);
		}
	}
	
	
	public void drawScreen(int x, int y, float p_73863_3_)
	{
		super.drawScreen(x, y, p_73863_3_);
		// GL11.glEnable(GL11.GL_BLEND);
    	// GL11.glEnable(GL11.GL_ALPHA_TEST);
    	
		draw_background(gui_x, gui_y);
		draw_job_tab();
		draw_job_name(gui_x, gui_y);
		draw_skill_point(gui_x, gui_y);
		draw_skill_panel(bar_level);
		draw_skill_slot();
		draw_skill_panel_window_bar(gui_x + 127, gui_y + 24);
		
		if(is_dragging)
		{
			SkillTextureHelper.draw_skill_texture(dragging_skill_name, x - 8, y - 8, this.zLevel, 16, 16);
		}
		
		for(int n = 0; n < 4; n++) 
		{
			if(bar_level + n >= skill_panel_array.length)
	    		return;
    		if(bar_level < 0)
    			bar_level = 0;

			CustomGuiSkillPanel skill_panel = skill_panel_array[bar_level + n];
			if(skill_panel != null)
			{
				if(skill_panel.is_under_mouse_cursor(x, y))
				{
					SkillData skill_data = skill_panel.skill_data;
					draw_skill_information_window(x, y, skill_data);
					return;
				}
			}
		}
	}
	
    private void draw_background(int x, int y)
    {
    	// GL11.glEnable(GL11.GL_ALPHA_TEST);
        // GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glColor4f(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(skill_tree_texture);
    	this.drawTexturedModalRect(x, y, 0, 0, 150, 190);
    }
    
    private void draw_job_tab()
    {
    	job_tab_gui.draw();
    }
    
    private void draw_job_name(int x, int y)
    {
    	this.drawString(this.mc.fontRenderer, last_job_data.getString("name"), x + 7, y + 5, 0xFFFFFF);
    }
    
    private void draw_skill_point(int x, int y)
    {
    	this.drawString(this.mc.fontRenderer, "SP " + player_data.getString("skill-point"), x + 124, y + 3, 0xFFFFFF);
    }
    
    private void draw_skill_panel(int bar_level)
    {
    	for(int n = 0; n < 4; n++)
    	{
    		if(bar_level + n >= skill_panel_array.length)
	    		return;
    		if(bar_level < 0)
    			bar_level = 0;
    		
    		if(skill_panel_array[bar_level + n] != null)
    			skill_panel_array[bar_level + n].draw(n);
    	}
    }
    
    private void draw_skill_slot()
    {
    	for(int n = 0; n < skill_slot_array.length; n++)
    	{
    		skill_slot_array[n].draw();
    	}
    }

    private void draw_skill_panel_window_bar(int x, int y)
    {
		this.mc.getTextureManager().bindTexture(skill_tree_texture);
    	this.drawTexturedModalRect(x, (int)(y + (((float)(this.bar_level) / (float)(this.skill_panel_array.length - 4)) * (124 - 19))), 170, 66, 16, 19);
    }

    private void draw_skill_information_window(int x, int y, SkillData skill_data)
    {
    	ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int screen_width = scaledresolution.getScaledWidth();
		int screen_height = scaledresolution.getScaledHeight();
		
    	String skill_name = skill_data.skill_name;
    	int skill_current_level = skill_data.current_level;
    	int skill_max_level = skill_data.max_skill_level;
    	List<String> skill_lore_array = skill_data.skill_lore_array;
    	List<String> level_lore_array = skill_data.level_lore_array;
    	
    	int total_width = 139;
    	int total_height = 103;
    	
    	float max_string_width = 0;
    	int limit_string_width = 80;
    	
    	int max_string_height = 0;
    	int limit_string_height = 3;
    	
    	for(int n = 0; n < skill_lore_array.size(); n++)
    	{
    		String lore = skill_lore_array.get(n);
    		max_string_width = Math.max(max_string_width, (this.getStringWidth(this.mc.fontRenderer, lore) * 0.75f) - 13);
    		max_string_height++;
    	}
    	for(int n = 0; n < level_lore_array.size(); n++) 
    	{
    		String lore = level_lore_array.get(n);
    		max_string_width = Math.max(max_string_width, (this.getStringWidth(this.mc.fontRenderer, lore) * 0.75f) - 13);
    	}
    	// To Draw The Skill's Master Level
    	max_string_height += 1;
    	
    	total_width += Math.max((max_string_width - limit_string_width), 0);
    	total_height += Math.max((10 * (max_string_height - limit_string_height)), 0);
    	
    	if((x + total_width) > screen_width)
    		x = screen_width - total_width;
    	if((y + total_height) > screen_height)
    		y = screen_height - total_height;
    	
    	this.mc.getTextureManager().bindTexture(skill_information_texture);
    	
    	this.drawTexturedModalRect(x, y, 0, 0, 131, 54);
    	if(max_string_height > limit_string_height) 
    	{
    		for(int n = 0; n < (max_string_height - limit_string_height); n++) 
    		{
    	    	this.drawTexturedModalRect(x, y + 54 + (10 * n), 0, 49, 131, 5);
    	    	this.drawTexturedModalRect(x, y + 54 + (10 * n) + 5, 0, 49, 131, 5);
    		}
    		this.drawTexturedModalRect(x, y + 54 + (10 * (max_string_height - limit_string_height)), 0, 54, 131, 49);
    	}
    	else 
    	{
    		this.drawTexturedModalRect(x, y + 54, 0, 54, 132, 49);
    	}
    	
    	if(max_string_width > limit_string_width) 
    	{
        	for(int n = 0; n < (max_string_width - limit_string_width); n++) 
        	{
            	this.drawTexturedModalRect(x + n + 131, y, 131, 0, 1, 54);
            	if(max_string_height > limit_string_height) 
            	{
            		for(int m = 0; m < (max_string_height - limit_string_height); m++) 
            		{
                    	this.drawTexturedModalRect(x + n + 131, y + 54 + (10 * m), 131, 49, 1, 5);
                    	this.drawTexturedModalRect(x + n + 131, y + 54 + (10 * m) + 5, 131, 49, 1, 5);
            		}
            		this.drawTexturedModalRect(x + n + 131, y + 54 + (10 * (max_string_height - limit_string_height)), 131, 54, 1, 49);
            	}
            	else
            	{
            		this.drawTexturedModalRect(x + n + 131, y + 54, 131, 54, 1, 49);
            	}
        	}
        	this.drawTexturedModalRect(x + (max_string_width - limit_string_width) + 131, y, 132, 0, 7, 54);
        	if(max_string_height > limit_string_height) 
        	{
        		for(int n = 0; n < (max_string_height - limit_string_height); n++) 
        		{
                	this.drawTexturedModalRect(x + (max_string_width - limit_string_width) + 131, y + 54 + (10 * n), 132, 49, 7, 5);
                	this.drawTexturedModalRect(x + (max_string_width - limit_string_width) + 131, y + 54 + (10 * n) + 5, 132, 49, 7, 5);
        		}
        		this.drawTexturedModalRect(x + (max_string_width - limit_string_width) + 131, y + 54 + (10 * (max_string_height - limit_string_height)), 132, 54, 7, 49);
        	}
        	else
        	{
            	this.drawTexturedModalRect(x + (max_string_width - limit_string_width) + 131, y + 54, 132, 54, 7, 49);
        	}
    	}
    	else
    	{
    		this.drawTexturedModalRect(x + 131, y, 131, 0, 8, 54);
        	if(max_string_height > limit_string_height) 
        	{
        		for(int n = 0; n < (max_string_height - limit_string_height); n++) 
        		{
                	this.drawTexturedModalRect(x + 131, y + 54 + (10 * n), 131, 49, 8, 5);
                	this.drawTexturedModalRect(x + 131, y + 54 + (10 * n) + 5, 131, 49, 8, 5);
        		}
        		this.drawTexturedModalRect(x + 131, y + 54 + (10 * (max_string_height - limit_string_height)), 131, 54, 8, 49);
        	}
        	else
        	{
            	this.drawTexturedModalRect(x + 131, y + 54, 131, 54, 8, 49);
        	}
    	}
    	
    	// skill name
    	this.drawCenteredString(this.mc.fontRenderer, skill_name, x + (total_width / 2), y + 4, 0xD3BC13, 1f);
		
    	// current level
    	this.drawString(this.mc.fontRenderer, "[Lv. " + skill_current_level + "]", x + (total_width / 2) + (getStringWidth(this.mc.fontRenderer, skill_name) / 2 * 0.8f) + 3, y + 4 + 4, 0xD3BC13, 0.5f);
    	
    	// master level
    	this.drawString(this.mc.fontRenderer, "[Master Lv. " + skill_max_level + "]", x + 43, y + 18, 0xFFFFFF, 0.75f);
    	
    	SkillTextureHelper.draw_skill_texture(skill_name, x + 11, y + 19, this.zLevel, 27, 27);
    	
    	this.mc.getTextureManager().bindTexture(skill_information_texture);
    	this.drawTexturedModalRect(x + 10, y + 18, 0, 103, 29, 29);
    	
    	// skill lore
    	for(int n = 0; n < skill_lore_array.size(); n++) 
    	{
    		String lore = skill_lore_array.get(n);
        	this.drawString(this.mc.fontRenderer, lore, x + 43, y + 28 + (10 * n), 0xFFFFFF, 0.75f);
    	}
    	
    	this.drawString(this.mc.fontRenderer, (skill_current_level == skill_max_level) ? "[Master Lv.]" : ("[Current Lv. " + skill_current_level + "]"), x + 4, y + 58 + Math.max((10 * (max_string_height - limit_string_height)), 0), 0xFFFFFF, 0.75f);
    	try {
        	this.drawString(this.mc.fontRenderer, level_lore_array.get(skill_current_level - 1), x + 4, y + 58 + 8 + Math.max((10 * (max_string_height - limit_string_height)), 0), 0xFFFFFF, 0.75f);
    	}
    	catch(Exception e) {
    		
    	}
    	
    	if(skill_current_level < skill_max_level) 
    	{
    		this.drawString(this.mc.fontRenderer, (skill_current_level + 1 == skill_max_level) ? "[Master Lv.]" : ("[Next Lv. " + (skill_current_level + 1) + "]"), x + 4, y + 58 + (8 * 3) + Math.max((10 * (max_string_height - limit_string_height)), 0), 0xFFFFFF, 0.75f);
        	try {
        		this.drawString(this.mc.fontRenderer, level_lore_array.get((skill_current_level)), x + 4, y + 58 + (8 * 4) + Math.max((10 * (max_string_height - limit_string_height)), 0), 0xFFFFFF, 0.75f);
        	}
        	catch(Exception e) {
        		
        	}
    	}
    }
}
