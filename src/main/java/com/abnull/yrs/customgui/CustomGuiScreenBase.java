package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public abstract class CustomGuiScreenBase extends GuiScreen {

	public CustomGuiScreenBase()
	{
		this.mc = Minecraft.getMinecraft();
		this.fontRendererObj = this.mc.fontRenderer;
	}
	
	public void drawTexturedModalRect(float p_73729_1_, float p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(p_73729_1_, p_73729_2_, 1);
		super.drawTexturedModalRect(0, 0, p_73729_3_, p_73729_4_, p_73729_5_, p_73729_6_);
		GL11.glPopMatrix();
	}
	
	public void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, float p_73732_3_, float p_73732_4_, int p_73732_5_)
    {
		GL11.glPushMatrix();
		GL11.glTranslatef(p_73732_3_, p_73732_4_, 1);
		super.drawCenteredString(p_73732_1_, p_73732_2_, 0, 0, p_73732_5_);
		GL11.glPopMatrix();
    }
	
	public void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, float p_73732_3_, float p_73732_4_, int p_73732_5_, float size)
    {
		GL11.glPushMatrix();
		GL11.glScalef(size, size, 1);
		GL11.glTranslatef(p_73732_3_ * (1 / size), p_73732_4_ * (1 / size), 0);
		super.drawCenteredString(p_73732_1_, p_73732_2_, 0, 0, p_73732_5_);
		GL11.glScalef(1 / size, 1 / size, 1);
		GL11.glPopMatrix();
    }
	
	public void drawString(FontRenderer p_73731_1_, String p_73731_2_, float p_73731_3_, float p_73731_4_, int p_73731_5_)
    {
		GL11.glPushMatrix();
		GL11.glTranslatef(p_73731_3_, p_73731_4_, 1);
        p_73731_1_.drawStringWithShadow(p_73731_2_, 0, 0, p_73731_5_);
		GL11.glPopMatrix();
    }
	
	public void drawString(FontRenderer p_73731_1_, String p_73731_2_, float p_73731_3_, float p_73731_4_, int p_73731_5_, float size)
    {
		GL11.glPushMatrix();
		GL11.glScalef(size, size, 1);
		GL11.glTranslatef(p_73731_3_ * (1 / size), p_73731_4_ * (1 / size), 0);
        p_73731_1_.drawStringWithShadow(p_73731_2_, 0, 0, p_73731_5_);
		GL11.glScalef(1 / size, 1 / size, 1);
		GL11.glPopMatrix();
    }
	
	public int getStringWidth(FontRenderer fontrenderer, String string) 
	{
		return fontrenderer.getStringWidth(string);
	}
}
