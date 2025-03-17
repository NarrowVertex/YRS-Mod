package com.abnull.yrs.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.abnull.yrs.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SkillTextureHelper {

	public static final ResourceLocation blank_resource_location = new ResourceLocation("yrs", "textures/blank.png");
	
	public static void draw_skill_texture(String skill_name, float coordi_x, float coordi_y, float z_level, int coordi_width, int coordi_height)
	{
		SkillData skill_data = ClientProxy.skill_data_map.get(skill_name);
		
		if(skill_data == null)
			return;
		
		if(!ClientProxy.skill_texture_data.hasKey(skill_name))
		{
			System.out.println("Can't Find Skill Texture Data By Name [" + skill_name + "]. . .");
			return;
		}
		
		NBTTagCompound skill_texture_data = ClientProxy.skill_texture_data.getCompoundTag(skill_name);
				
		int texture_width = skill_texture_data.getInteger("width");
		int texture_height = skill_texture_data.getInteger("height");
		
		float block_width = (float)coordi_width / (float)texture_width;
		float block_height = (float)coordi_height / (float)texture_height;
		GL11.glColor3f(1, 1, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
        Tessellator tessellator = Tessellator.instance;
        
		int point_x = 0;
		
		float f = 0.00390625F;
        float f1 = 0.00390625F;
        
		for(float x = 0; x < coordi_width; x += block_width, point_x++)
		{
			int point_y = 0;
			for(float y = 0; y < coordi_height; y += block_height, point_y++)
			{
				int color_integer = skill_texture_data.getInteger(point_x + "," + point_y);
				Color color = new Color(color_integer);
				float red = ((float)color.getRed()) / 255.0f;
				float green = ((float)color.getGreen()) / 255.0f;
				float blue = ((float)color.getBlue()) / 255.0f;
				
				GL11.glColor3f(red, green, blue);
				
				float p_73729_1_ = coordi_x + x;
				float p_73729_2_ = coordi_y + y;
				
		        tessellator.startDrawingQuads();
		        tessellator.addVertex((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 1), (double)z_level);
		        tessellator.addVertex((double)(p_73729_1_ + 1), (double)(p_73729_2_ + 1), (double)z_level);
		        tessellator.addVertex((double)(p_73729_1_ + 1), (double)(p_73729_2_ + 0), (double)z_level);
		        tessellator.addVertex((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)z_level);
		        tessellator.draw();
			}
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		if(!draw_skill_cooltime(coordi_x, coordi_y, z_level, coordi_width, skill_name, skill_data))
		{
			draw_not_enough_mana(coordi_x, coordi_y, z_level, coordi_width, skill_data);
		}
		
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public static void draw_not_enough_mana(float x, float y, float z_level, int size, SkillData skill_data)
	{
		int player_mp = Integer.parseInt(ClientProxy.player_data.getString("mp"));
		int skill_mp = skill_data.cost_mana;
		
		if(player_mp < skill_mp)
		{
			GL11.glColor4f(0, 0f, 0f, 0.6f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(SkillTextureHelper.blank_resource_location);
			
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV((double)(x +    0), (double)(y + size), (double)z_level, 0, 1);
	        tessellator.addVertexWithUV((double)(x + size), (double)(y + size), (double)z_level, 1, 1);
	        tessellator.addVertexWithUV((double)(x + size), (double)(y +    0), (double)z_level, 1, 0);
	        tessellator.addVertexWithUV((double)(x +    0), (double)(y +    0), (double)z_level, 0, 0);
	        tessellator.draw();
		}
	}
	
	public static boolean draw_skill_cooltime(float x, float y, float z_level, int size, String skill_name, SkillData skill_data)
	{		
		if(skill_data == null)
			return false;
		
		double skill_cooltime = skill_data.cooltime;
		
		if(!ClientProxy.skill_cooltime_map.containsKey(skill_name))
		{
			ClientProxy.skill_cooltime_map.put(skill_name, 0L);
		}
		
		double current_time = (double)System.currentTimeMillis() / 1000d;
		double last_skill_used_time = (double)ClientProxy.skill_cooltime_map.get(skill_name) / 1000d;
		if((current_time - last_skill_used_time) < skill_cooltime)
		{
			double left_time = (last_skill_used_time + (skill_cooltime)) - current_time;
			double percentage = left_time / skill_cooltime;
			
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			GL11.glColor4f(0, 0f, 0f, 0.6f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(SkillTextureHelper.blank_resource_location);
			
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV((double)(x +    0), (double)(y + size), (double)z_level, 0, 1);
	        tessellator.addVertexWithUV((double)(x + size), (double)(y + size), (double)z_level, 1, 1);
	        tessellator.addVertexWithUV((double)(x + size), (double)(y +    0), (double)z_level, 1, 0);
	        tessellator.addVertexWithUV((double)(x +    0), (double)(y +    0), (double)z_level, 0, 0);
	        tessellator.draw();
			
			
			GL11.glColor4f(0, 0, 0, 0.85f);
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
			
	        GL11.glColor4f(1, 1, 1, 1);
	        
	        GL11.glPushMatrix();
	        GL11.glTranslatef((x + size / 2) - Minecraft.getMinecraft().fontRenderer.getStringWidth("" + (int)left_time) / 2, y + 4, 0);
	        Minecraft.getMinecraft().fontRenderer.drawString("" + (int)left_time, 0, 0, 0xFFFFFF, false);
	        GL11.glPopMatrix();

			GL11.glColor4f(1, 1, 1, 1);
			
			return true;
		}
		return false;
	}
}
