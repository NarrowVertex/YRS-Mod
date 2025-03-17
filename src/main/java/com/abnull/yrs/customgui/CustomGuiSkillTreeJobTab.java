package com.abnull.yrs.customgui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CustomGuiSkillTreeJobTab extends CustomGuiTabBase {
	
	public CustomGuiSkillTreeJobTab(int gui_x, int gui_y, NBTTagCompound data)
	{
		super(gui_x - 30, gui_y + 15, 
				0, 25,
				169, 25, 
				169, 0, 
				33, 25, 
				data, 
				CustomGuiSkillTree.skill_tree_texture);
	}
}
