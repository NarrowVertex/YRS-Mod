package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class CustomGuiSkillUpgradeButton extends CustomGuiButtonBase {

	public CustomGuiSkillUpgradeButton(int id) {
		super(id, 0, 0, 20, 222, 9, 9, CustomGuiSkillTree.skill_tree_texture);
	}
	
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_, int x, int y, boolean is_actived)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.u = is_actived ? 20 : 29;
		super.drawButton(p_146112_1_, p_146112_2_, p_146112_3_);
	}
}
