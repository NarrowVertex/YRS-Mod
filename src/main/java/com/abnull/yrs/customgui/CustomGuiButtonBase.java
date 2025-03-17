package com.abnull.yrs.customgui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public abstract class CustomGuiButtonBase extends GuiButton {

	ResourceLocation button_resource_location;
	
	int u;
	int v;
	
	public CustomGuiButtonBase(int id, int x, int y, int u, int v, int width, int height, ResourceLocation p_i1021_4_) {
		super(id, x, y, width, height, "");
		this.u = u;
		this.v = v;
		this.button_resource_location = p_i1021_4_;
	}
	
	@Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
	{
		if (this.visible)
        {
			FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            p_146112_1_.getTextureManager().bindTexture(this.button_resource_location);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.u, this.v, this.width, this.height);
            
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            
            // Draw String => It can't use.
        }
	}
}
