package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;

public abstract class CustomGuiBase extends Gui implements ICustomGui {

	protected Minecraft mc;
	
	public CustomGuiBase(Minecraft m)
	{
		this.mc = m;
	}
	
	public void drawTexturedModalRect(float p_73729_1_, float p_73729_2_, float p_73729_3_, float p_73729_4_, float p_73729_5_, float p_73729_6_)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.draw();
    }
	
	public void drawCenteredStringWithShadow(FontRenderer p_73732_1_, String p_73732_2_, float f, float g, int p_73732_5_, int p_73732_6_)
    {
        p_73732_1_.drawString(p_73732_2_, (int)f - p_73732_1_.getStringWidth(p_73732_2_) / 2 + 1, (int) (g + 1), p_73732_6_);
        p_73732_1_.drawString(p_73732_2_, (int)f - p_73732_1_.getStringWidth(p_73732_2_) / 2, (int) g, p_73732_5_);
    }
	
	public void drawCenteredStringWithShadowAndThickness(FontRenderer p_73732_1_, String p_73732_2_, float f, float g, int p_73732_5_, int p_73732_6_, float thickness)
    {
		GL11.glTranslatef(thickness, thickness, 0);
        p_73732_1_.drawString(p_73732_2_, (int)f - p_73732_1_.getStringWidth(p_73732_2_) / 2, (int) (g), p_73732_6_);
		GL11.glTranslatef(-thickness, -thickness, 0);
        p_73732_1_.drawString(p_73732_2_, (int)f - p_73732_1_.getStringWidth(p_73732_2_) / 2, (int) g, p_73732_5_);
    }
	
	public void drawString(FontRenderer f, String s, float x, float y, int c, boolean i, float p)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glScalef(p, p, 1);
		f.drawString(s, 0, 0, c, i);
		GL11.glScalef((1 / p), (1 / p), 1);
		GL11.glPopMatrix();
	}
	
	public void drawCenteredString(FontRenderer fontrenderer, String string, float x, float y, int color, boolean is_shadow, float size)
    {
        this.drawString(fontrenderer, string, x - fontrenderer.getStringWidth(string) / 2, y, color, is_shadow, size);
    }
}
