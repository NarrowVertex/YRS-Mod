package com.abnull.yrs.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class DrawDurationHelper {

	public static final ResourceLocation blank_resource_location = new ResourceLocation("yrs", "textures/blank.png");
	
	public DrawDurationHelper() 
	{
		
	}
	
	public static void draw_clock_type(float x, float y, float z_level, float size, float percentage)
	{
		GL11.glColor4f(0, 0, 0, 0.6f);
		draw_clock_type_with_resource(x, y, z_level, size, percentage, blank_resource_location);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public static void draw_clock_type_with_resource(float x, float y, float z_level, float size, float percentage, ResourceLocation r)
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(r);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glScalef(size, size, 1);
		
		Tessellator t = Tessellator.instance;
        t.startDrawing(GL11.GL_POLYGON);
        t.addVertex(0.5, 0.5, z_level);
        for(int n = 0; n < 360 * percentage; n++)
        {
        	int angle = n - 90;
        	
        	double base_length = 0.5;
        	boolean is_side = false;
        	boolean is_x_over_zero = true;
        	boolean is_y_over_zero = true;
        	
        	double point_x, point_y;
        	
        	if(angle > -90 + 45 * 1)
        	{
        		is_side = true;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 2)
        	{
        		is_side = true;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 3)
        	{
        		is_side = false;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 4)
        	{
        		is_side = false;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 5)
        	{
        		is_side = true;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	if(angle > -90 + 45 * 6)
        	{
        		is_side = true;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	if(angle > -90 + 45 * 7)
        	{
        		is_side = false;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	
        	if(is_side)
        	{
        		point_x = base_length * (is_x_over_zero ? 1 : -1);
        		point_y = base_length * (is_y_over_zero ? -1 : 1) * (Math.sin(Math.toRadians(angle)) / Math.cos(Math.toRadians(angle)));
        	}
        	else
        	{
        		point_x = base_length * (is_x_over_zero ? 1 : -1) * (Math.cos(Math.toRadians(angle)) / Math.sin(Math.toRadians(angle)));
        		point_y = base_length * (is_y_over_zero ? -1 : 1);
        	}
        	t.addVertex(0.5 + point_x, 0.5 + point_y, z_level);
        }
        t.draw();
		
		GL11.glScalef(1 / (float)size, 1 / (float)size, 1);
        GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	public static void draw_clock_type_with_UV(float x, float y, float z_level, float size, float percentage, ResourceLocation r, float u, float v, float t_w, float t_h)
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(r);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		
        float f = 0.00390625F;
		Tessellator t = Tessellator.instance;
        t.startDrawing(GL11.GL_POLYGON);
        t.addVertexWithUV(size * 0.5, size * 0.5, z_level, (u + (t_w * 0.5f)) * f, (v + (t_h * 0.5f)) * f);
        for(int n = 0; n < 360 * percentage; n++)
        {
        	int angle = n - 90;
        	
        	double base_length = 0.5;
        	boolean is_side = false;
        	boolean is_x_over_zero = true;
        	boolean is_y_over_zero = true;
        	
        	double point_x, point_y;
        	
        	if(angle > -90 + 45 * 1)
        	{
        		is_side = true;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 2)
        	{
        		is_side = true;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 3)
        	{
        		is_side = false;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 4)
        	{
        		is_side = false;
        		is_x_over_zero = false;
        		is_y_over_zero = false;
        	}
        	if(angle > -90 + 45 * 5)
        	{
        		is_side = true;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	if(angle > -90 + 45 * 6)
        	{
        		is_side = true;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	if(angle > -90 + 45 * 7)
        	{
        		is_side = false;
        		is_x_over_zero = true;
        		is_y_over_zero = true;
        	}
        	
        	if(is_side)
        	{
        		point_x = base_length * (is_x_over_zero ? 1 : -1);
        		point_y = base_length * (is_y_over_zero ? -1 : 1) * (Math.sin(Math.toRadians(angle)) / Math.cos(Math.toRadians(angle)));
        	}
        	else
        	{
        		point_x = base_length * (is_x_over_zero ? 1 : -1) * (Math.cos(Math.toRadians(angle)) / Math.sin(Math.toRadians(angle)));
        		point_y = base_length * (is_y_over_zero ? -1 : 1);
        	}
        	t.addVertexWithUV((0.5 + point_x) * size, (0.5 + point_y) * size, z_level, (u + (t_w * (0.5f + point_x))) * f, (v + (t_h * (0.5f + point_y))) * f);
        }
        t.draw();
		
        GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
