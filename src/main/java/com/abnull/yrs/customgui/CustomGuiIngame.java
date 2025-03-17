package com.abnull.yrs.customgui;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.proxy.ClientProxy;
import com.abnull.yrs.util.DrawDurationHelper;
import com.abnull.yrs.util.MathHelper;
import com.abnull.yrs.util.SkillData;
import com.abnull.yrs.util.SkillTextureHelper;

public class CustomGuiIngame extends CustomGuiBase implements ICustomGui {
	
	public static final ResourceLocation ingame_texture = new ResourceLocation("yrs", "textures/gui/ingame_gui.png");
	public static final ResourceLocation inventory_texutre = new ResourceLocation("textures/gui/container/inventory.png");
	
	protected static final RenderItem itemRenderer = new RenderItem();
    
	public static float gui_scale = 1;
	
	public int screen_width;
	public int screen_height;
	
	private float prev_hp;
    private float prev_damage_hp;
    
    private long last_been_hitted_time = 0;
    
    private float prev_mp = -1;
    private float prev_background_mp = -1;
    
    private long last_mp_variated_time = 0;
    
    private final long damage_hp_gui_delay_time = 1000;
    private final float variation = 0.2f;
	
    private boolean is_player_entering_region = false;
    private boolean was_player_entering_region = false;
    private String region_name = "";
    private float region_name_showing_time = 1;
    private float region_name_staying_time = 3;
    private long last_entered_region_time = 0;
    
	public CustomGuiIngame(Minecraft m)
	{
		super(m);
		
		prev_hp = this.mc.thePlayer.getHealth();
		prev_damage_hp = this.mc.thePlayer.getHealth();
	}
	
	public void draw(float partial_ticks)
	{
		// render_player_model();
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		this.screen_width = scaledresolution.getScaledWidth();
		this.screen_height = scaledresolution.getScaledHeight();
		
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(((this.screen_width / 2) - 151 + 112) * 2, 0, 156, 142);
        
		// GL11.glEnable(GL11.GL_SCISSOR_TEST);
		// GL11.glScissor((this.screen_width / 2) - 44, this.screen_height - 78, 44, 30);
		// GL11.glScissor(0, 0, 10, 10);
		render_player_model();
		// GL11.glDisable(GL11.GL_SCISSOR_TEST);
		// GL11.glPopAttrib();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopAttrib();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
        
		
		
		draw_ingame_background();
		draw_health();
		draw_mana();
		draw_exp();
		draw_inventory(partial_ticks);
		draw_hunger();
		draw_armor();
		draw_oxygen();
		draw_skill_slot();
		draw_potion_slot(partial_ticks);
		draw_buff_effect();
		draw_region_title_to_player_entering_region();
		
	}
	
	public void draw_ingame_background()
	{
		this.mc.getTextureManager().bindTexture(ingame_texture);
		this.drawTexturedModalRect((this.screen_width / 2) - 151, screen_height - 78, 0, 0, 151, 78);
        this.drawTexturedModalRect((this.screen_width / 2), screen_height - 78, 0, 78, 151, 78);
	}
	
	public void draw_health()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        float max_hp = this.mc.thePlayer.getMaxHealth();
        float hp = this.mc.thePlayer.getHealth();
        
        this.drawTexturedModalRect((this.screen_width / 2) - 149, this.screen_height - 60, 152, 192, 58, 58);
        
        prev_hp = Math.min(prev_hp, max_hp);
        prev_damage_hp = Math.min(prev_damage_hp, max_hp);
        
        if(hp != prev_hp)
        {
        	if(hp < prev_hp)
        	{
        		last_been_hitted_time =  System.currentTimeMillis();
        	}
        	
        	if(Math.abs(hp - prev_hp) >= variation * (max_hp / 20))
            {
            	if(hp > prev_hp)
            		prev_hp += variation * (max_hp / 20);
	            else if(hp < prev_hp)
	            	prev_hp -= variation * (max_hp / 20);
            }
            else if(Math.abs(hp - prev_hp) < variation * (max_hp / 20))
            {
            	prev_hp = hp;
            }
        }
        
        if(Math.abs( System.currentTimeMillis() - last_been_hitted_time) > damage_hp_gui_delay_time)
        {
        	if(hp != prev_damage_hp)
            {
            	if(Math.abs(hp - prev_damage_hp) >= variation * (max_hp / 20))
                {
                	if(hp > prev_damage_hp)
                		prev_damage_hp += variation * (max_hp / 20);
    	            else if(hp < prev_damage_hp)
    	            	prev_damage_hp -= variation * (max_hp / 20);
                }
                else if(Math.abs(hp - prev_damage_hp) < variation * (max_hp / 20))
                {
                	prev_damage_hp = hp;
                }
            }
        }

        GL11.glColor3f(0.4f, 0.1f, 0.1f);
        // damage hp orb
        this.drawTexturedModalRect((this.screen_width / 2) - 149, this.screen_height - 60 + 58 * (1 - (prev_damage_hp / max_hp)), 151, 0 + 58 * (1 - (prev_damage_hp / max_hp)), 58, 58 * (prev_damage_hp / max_hp));
        GL11.glColor3f(1f, 1f, 1f);
        
        // current hp orb
        this.drawTexturedModalRect((this.screen_width / 2) - 149, this.screen_height - 60 + 58 * (1 - (prev_hp / max_hp)), 151, 0 + 58 * (1 - (prev_hp / max_hp)), 58, 58 * (prev_hp / max_hp));
        
        this.drawTexturedModalRect((this.screen_width / 2) - 149, this.screen_height - 60, 151, 116, 58, 58);
        this.drawTexturedModalRect((this.screen_width / 2) - 151 + 47, this.screen_height - 78 + 68, 70, 214, 16, 5);
        GL11.glTranslatef(-1f, 1f, 0);
        GL11.glScalef(0.7f, 0.7f, 0.7f);
        
        int hp_dummy = (int)hp;
        if(hp < 1.0f && hp > 0.0f)
        {
        	hp_dummy = 1;
        }
        this.drawCenteredStringWithShadow(this.mc.fontRenderer, (int)hp_dummy + "/" + (int)max_hp, ((this.screen_width / 2) - 149 + 30) * 1.4285742857f, (this.screen_height - 60 + 25) * 1.4285742857f, 0xFFFFFF, 0x000000);
        GL11.glScalef(1.4285742857f, 1.4285742857f, 1.4285742857f);
        GL11.glColor3f(1, 1, 1);
        GL11.glTranslatef(1f, -1f, 0);
        GL11.glEnable(GL11.GL_BLEND);
	}
	
	public void draw_mana()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        this.drawTexturedModalRect((this.screen_width / 2) + 91, this.screen_height - 60, 152, 192, 58, 58);
        if(ClientProxy.player_data.getString("mp") != null && ClientProxy.player_data.getString("max-mp") != null && !ClientProxy.player_data.getString("mp").equals("") && !ClientProxy.player_data.getString("max-mp").equals(""))
        {
        	prev_mp = (prev_mp == -1) ? Integer.parseInt(ClientProxy.player_data.getString("mp")) : prev_mp;
        	prev_background_mp = (prev_background_mp == -1) ? Integer.parseInt(ClientProxy.player_data.getString("mp")) : prev_background_mp;
        	
        	int max_mp = Integer.parseInt(ClientProxy.player_data.getString("max-mp"));
        	int mp = Integer.parseInt(ClientProxy.player_data.getString("mp"));
        	
        	prev_mp = Math.min(prev_mp, max_mp);
        	prev_background_mp = Math.min(prev_background_mp, max_mp);
        	
        	if(mp != prev_mp)
            {
            	if(mp < prev_mp)
            	{
            		last_mp_variated_time =  System.currentTimeMillis();
            	}
            	
            	if(Math.abs(mp - prev_mp) >= variation * (max_mp / 20))
                {
                	if(mp > prev_mp)
                		prev_mp += variation * (max_mp / 20);
    	            else if(mp < prev_mp)
    	            	prev_mp -= variation * (max_mp / 20);
                }
                else if(Math.abs(mp - prev_mp) < variation * (max_mp / 20))
                {
                	prev_mp = mp;
                }
            }
            
            if(Math.abs( System.currentTimeMillis() - last_mp_variated_time) > damage_hp_gui_delay_time)
            {
            	if(mp != prev_background_mp)
                {
                	if(Math.abs(mp - prev_background_mp) >= variation * (max_mp / 20))
                    {
                    	if(mp > prev_background_mp)
                    		prev_background_mp += variation * (max_mp / 20);
        	            else if(mp < prev_background_mp)
        	            	prev_background_mp -= variation * (max_mp / 20);
                    }
                    else if(Math.abs(mp - prev_background_mp) < variation * (max_mp / 20))
                    {
                    	prev_background_mp = mp;
                    }
                }
            }
            
            // background mp
            GL11.glColor3f(0.1f, 0.1f, 0.4f);
        	this.drawTexturedModalRect((this.screen_width / 2) + 91, this.screen_height - 60 + 58 * (1 - ((float)prev_background_mp / (float)max_mp)), 151, 58 + 58 * (1 - ((float)prev_background_mp / (float)max_mp)), 58, 58 * ((float)prev_background_mp / (float)max_mp));
            GL11.glColor3f(1, 1, 1);
        	
            // current mp
        	this.drawTexturedModalRect((this.screen_width / 2) + 91, this.screen_height - 60 + 58 * (1 - ((float)prev_mp / (float)max_mp)), 151, 58 + 58 * (1 - ((float)prev_mp / (float)max_mp)), 58, 58 * ((float)prev_mp / (float)max_mp));
        }
        this.drawTexturedModalRect((this.screen_width / 2) + 91, this.screen_height - 60, 151, 116, 58, 58);
        this.drawTexturedModalRect((this.screen_width / 2) + 87, this.screen_height - 78 + 68, 86, 214, 16, 5);
        if(ClientProxy.player_data.getString("mp") != null && ClientProxy.player_data.getString("max-mp") != null && !ClientProxy.player_data.getString("mp").equals("") && !ClientProxy.player_data.getString("max-mp").equals(""))
        {
        	int max_mp = Integer.parseInt(ClientProxy.player_data.getString("max-mp"));
        	int mp = Integer.parseInt(ClientProxy.player_data.getString("mp"));
        	GL11.glTranslatef(-1, 1, 0);
        	GL11.glScalef(0.7f, 0.7f, 0.7f);
            this.drawCenteredStringWithShadow(this.mc.fontRenderer, mp + "/" + max_mp, ((this.screen_width / 2) + 92 + 29) * 1.4285742857f, (this.screen_height - 60 + 25) * 1.4285742857f, 0xFFFFFF, 0x000000);
            GL11.glScalef(1.4285742857f, 1.4285742857f, 1.4285742857f);
        	GL11.glTranslatef(1, -1, 0);
            GL11.glColor3f(1, 1, 1);
        }
	}
	
	public void draw_exp()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
		
		this.drawTexturedModalRect((this.screen_width / 2) - 151, this.screen_height - 6, 0, 156, 151, 6);
        this.drawTexturedModalRect((this.screen_width / 2), this.screen_height - 6, 0, 156 + 6, 151, 6);
        
        if(ClientProxy.player_data.hasKey("exp") && ClientProxy.player_data.hasKey("max-exp") && ClientProxy.player_data.hasKey("exp-level"))
        {
        	int level = Integer.parseInt(ClientProxy.player_data.getString("exp-level"));
    		long max_exp = Long.parseLong(ClientProxy.player_data.getString("max-exp"));
    		long exp = Long.parseLong(ClientProxy.player_data.getString("exp"));
    		
        	double exp_percent = (double)exp / (double)max_exp;
            if(exp_percent < 0.5f)
            {
                this.drawTexturedModalRect((this.screen_width / 2) - 151 + 41, this.screen_height - 6, 41, 168, (float) (110 * exp_percent * 2), 6);
            }
            else
            {
                this.drawTexturedModalRect((this.screen_width / 2) - 151 + 41, this.screen_height - 6, 41, 168, 110, 6);
                this.drawTexturedModalRect((this.screen_width / 2) - 151 + 41 + 110, this.screen_height - 6, 0, 168 + 6, 110 * (float)(exp_percent - 0.5d) * 2, 6);
            }
            this.drawTexturedModalRect((this.screen_width / 2) - 8, this.screen_height - 10, 102, 214, 16, 5);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            this.drawCenteredStringWithShadowAndThickness(this.mc.fontRenderer, (long)exp + " (" + (float)((float)Math.round(exp_percent * 10000) / 100.00f) + "%)", (this.screen_width / 2) * 2, (this.screen_height - 6 + 1) * 2, 0xFFFFFF, 0x000000, 0.5f);
            GL11.glScalef(2f, 2f, 2f);
            
            this.drawCenteredString(this.mc.fontRenderer, "Lv." + level, (this.screen_width / 2), this.screen_height - 75, 0xFFFFFF);
            GL11.glColor4f(1, 1, 1, 1);
        }
	}
	
	public void draw_inventory(float partial_ticks)
	{
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        
		// slot
        Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        this.drawTexturedModalRect((this.screen_width / 2) - 76, this.screen_height - 26, 0, 180, 153, 16);
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        RenderHelper.enableGUIStandardItemLighting();
        float radius = 0.8f;
        float reverse_radius = 1 / radius;
        GL11.glScalef(radius, radius, 1);
        for (int n = 0; n < 9; n++)
        {
            this.renderInventorySlot(n, ((this.screen_width / 2) - 76 + (17 * n)) * reverse_radius + ((1 - radius) * 16 / 2), (this.screen_height - 26) * reverse_radius + ((1 - radius) * 16 / 2), partial_ticks);
        }
        GL11.glScalef(reverse_radius, reverse_radius, 1);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glColor4f(1, 1, 1, 1);
        
        // slot_select
        Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        this.drawTexturedModalRect((this.screen_width / 2) - 76 + inventoryplayer.currentItem * 17 - 0.5f, this.screen_height - 26 - 0.5f, 53, 214, 17, 17);
        GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void draw_hunger()
	{
        Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        this.drawTexturedModalRect((this.screen_width / 2) + 34 + 8, this.screen_height - 52 - 19, 53 + 18, 196, 18, 18);
        this.drawTexturedModalRect((this.screen_width / 2) + 34 + 8, this.screen_height - 52 - 19 + 18 * (float)(1.0f - (float)this.mc.thePlayer.getFoodStats().getFoodLevel() / 20.0f), 53, 196 + 18 * (float)(1.0f - (float)this.mc.thePlayer.getFoodStats().getFoodLevel() / 20.0f), 18, 18 * (float)((float)this.mc.thePlayer.getFoodStats().getFoodLevel() / 20.0f));
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawCenteredString(this.mc.fontRenderer, (this.mc.thePlayer.getFoodStats().getFoodLevel() * 5) + "/100", ((this.screen_width / 2) + 34 + 9 + 8) * 2, (this.screen_height - 52 - 19 + 3) * 2, 0xFFFFFF);
        GL11.glScalef(2f, 2f, 2f);
	}

	public void draw_armor()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        this.drawTexturedModalRect((this.screen_width / 2) + 34 + 9 + 19, this.screen_height - 52 - 19, 89 + 18, 196, 18, 18);
        this.drawTexturedModalRect((this.screen_width / 2) + 34 + 9 + 19, this.screen_height - 52 - 19 + 18 * (float)(1.0f - (float)this.mc.thePlayer.getTotalArmorValue() * 5f / 100.0f), 89, 196 + 18 * (float)(1.0f - (float)this.mc.thePlayer.getTotalArmorValue() * 5f / 100.0f), 18, 18 * (float)(float)this.mc.thePlayer.getTotalArmorValue() * 5f / 100.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawCenteredString(this.mc.fontRenderer, (this.mc.thePlayer.getTotalArmorValue() * 5) + "/100", ((this.screen_width / 2) + 34 + 9 + 9 + 19) * 2, (this.screen_height - 52 - 19 + 3) * 2, 0xFFFFFF);
        GL11.glScalef(2f, 2f, 2f);
	}
	
	public void draw_oxygen()
	{
		if(this.mc.thePlayer.isEntityAlive() && this.mc.thePlayer.isInsideOfMaterial(Material.water))
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
			this.drawTexturedModalRect((this.screen_width / 2) + 34 + 10 + 38, this.screen_height - 52 - 19, 125, 196 + 18, 18, 18);
	        this.drawTexturedModalRect((this.screen_width / 2) + 34 + 10 + 38, this.screen_height - 52 - 19 + 18 * (float)(1.0f - ((float)this.mc.thePlayer.getAir() > 0 ? (float)this.mc.thePlayer.getAir() : 0) / 300.0f), 125, 196 + 18 * (float)(1.0f - ((float)this.mc.thePlayer.getAir() > 0 ? (float)this.mc.thePlayer.getAir() : 0) / 300.0f), 18, 18 * (((float)this.mc.thePlayer.getAir() > 0 ? (float)this.mc.thePlayer.getAir() : 0) / 300.0f));
	        GL11.glScalef(0.5f, 0.5f, 0.5f);
	        this.drawCenteredString(this.mc.fontRenderer, (int)(((float)this.mc.thePlayer.getAir() > 0 ? (float)this.mc.thePlayer.getAir() : 0) / 3.0f) + "/100", ((this.screen_width / 2) + 34 + 9 + 10 + 38) * 2, (this.screen_height - 52 - 19 + 3) * 2, 0xFFFFFF);
	        GL11.glScalef(2f, 2f, 2f);
		}
	}
	
	public void draw_skill_slot()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
		
		int base_x = (this.screen_width / 2) - 87;
		int base_y = this.screen_height - 71;
		
		
		this.drawTexturedModalRect(base_x, base_y, 0, 196, 53, 17);
		this.drawTexturedModalRect(base_x, base_y + 18, 0, 196, 53, 17);
		
		if(ClientProxy.player_data.hasKey("skill_slot"))
		{
			NBTTagCompound skill_slot_array_data = ClientProxy.player_data.getCompoundTag("skill_slot");
			for(int n = 0; n < 3; n++)
			{
				String skill_name = skill_slot_array_data.getString("slot" + (n + 1));
				if(!skill_name.equals("n"))
				{
					SkillTextureHelper.draw_skill_texture(skill_name, base_x + (n * 18), base_y + 1, this.zLevel, 16, 16);
				}
				else
				{
					;
				}
			}
			
			for(int n = 3; n < 6; n++)
			{
				String skill_name = skill_slot_array_data.getString("slot" + (n + 1));
				if(!skill_name.equals("n"))
				{
					SkillTextureHelper.draw_skill_texture(skill_name, base_x + ((n - 3) * 18), base_y + 1 + 18, this.zLevel, 16, 16);
				}
				else
				{
					;
				}
			}
		}
		// GL11.glColor3f(0.5f, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
		this.drawTexturedModalRect(base_x, base_y, 0, 214, 53, 17);
		this.drawTexturedModalRect(base_x, base_y + 18, 0, 214, 53, 17);
	}
	
	public void draw_skill_cooltime(int x, int y, int size, String skill_name)
	{
		SkillData skill_data = ClientProxy.skill_data_map.get(skill_name);
		
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
			this.mc.getTextureManager().bindTexture(SkillTextureHelper.blank_resource_location);
			this.drawTexturedModalRect(x, y, 0, 0, 16, 16);
			
			GL11.glColor4f(0, 0, 0, 0.85f);
			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glScalef(size, size, 1);
			
			Tessellator t = Tessellator.instance;
	        t.startDrawing(GL11.GL_POLYGON);
	        t.addVertex(0.5, 0.5, this.zLevel);
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
	        	t.addVertex(0.5 + point_x, 0.5 + point_y, this.zLevel);
	        }
	        t.draw();
			
			GL11.glScalef(1 / (float)size, 1 / (float)size, 1);
	        GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_CULL_FACE);
			
	        GL11.glColor4f(1, 1, 1, 1);
	        
	        this.drawCenteredString(this.mc.fontRenderer, "" + (int)left_time, x + size / 2, y + 4, 0xFFFFFF, false, 1f);
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
	public void draw_potion_slot(float partial_ticks)
	{
		if(!ClientProxy.player_data.hasKey("potion_tree_data"))
			return;
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
		this.drawTexturedModalRect((this.screen_width / 2) + 33, this.screen_height - 53, 0, 196, 53, 17);
		NBTTagCompound potion_slot_array_data = ClientProxy.player_data.getCompoundTag("potion_tree_data");
		for(int n = 0; n < 3; n++)
		{
			NBTTagCompound potion_tag = potion_slot_array_data.getCompoundTag("" + n);
			boolean is_null = potion_tag.getBoolean("is_null");
			
			if(!is_null)
			{
				ItemStack item_stack = new ItemStack(new Item());
				item_stack.readFromNBT(potion_tag.getCompoundTag("data"));
				this.render_item(item_stack, (this.screen_width / 2) + 33 + (n * 18), this.screen_height - 53, partial_ticks);
			}
		}
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glAlphaFunc(GL11.GL_ALWAYS, 0.0F);
	}
	
	public void draw_buff_effect() 
	{
        Collection collection = this.mc.thePlayer.getActivePotionEffects();
        
        if (!collection.isEmpty())
        {
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            
            float size = 0.7083f;
            float rv_size = 1 / size;
            GL11.glScalef(size, size, 1);
            
            
            int n = 0;
            for (Iterator iterator = this.mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); n++)
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                
                Minecraft.getMinecraft().getTextureManager().bindTexture(ingame_texture);
        		this.drawTexturedModalRect(this.screen_width * rv_size - 2 - 24 - (24 + 2) * n, 2, 0, 231, 24, 24);
        		
        		
                if (potion.hasStatusIcon())
                {
                    int l = potion.getStatusIconIndex();
                    
                    this.mc.getTextureManager().bindTexture(inventory_texutre);
                    this.drawTexturedModalRect(this.screen_width * rv_size - 2 - 24 + 3 - (24 + 2) * n, 2 + 3, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
                    
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glColor4f(0, 0, 0, 0.6f);
                    this.mc.getTextureManager().bindTexture(DrawDurationHelper.blank_resource_location);
                    this.drawTexturedModalRect(this.screen_width * rv_size - 2 - 24 + 3 - (24 + 2) * n, 2 + 3, 0, 0, 18, 18);
                    GL11.glColor4f(1, 1, 1, 1);
                }
                
                if(ClientProxy.granted_effect_duration_map.containsKey(potioneffect.getPotionID()))
                {
                	float potion_granted_duration = ClientProxy.granted_effect_duration_map.get(potioneffect.getPotionID());
                    float potion_current_duration = potioneffect.getDuration();
                    
                	if (potion.hasStatusIcon())
                    {
                        int l = potion.getStatusIconIndex();
                        
                        DrawDurationHelper.draw_clock_type_with_UV(this.screen_width * rv_size - 2 - 24 + 3 - (24 + 2) * n, 2 + 3, this.zLevel, 18, (potion_current_duration / potion_granted_duration), inventory_texutre, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
                    }
                	else
                	{
                        DrawDurationHelper.draw_clock_type(this.screen_width * rv_size - 2 - 24 + 3 - (24 + 2) * n, 2 + 3, this.zLevel, 18, (potion_current_duration / potion_granted_duration));
                	}
                	
                	GL11.glPushMatrix();
                	GL11.glTranslatef(this.screen_width * rv_size - 2 - 24 + 3 - (24 + 2) * n + 9, 5 + 10, 0);
                	GL11.glScalef(0.75f, 0.75f, 1);
                	this.drawCenteredString(this.mc.fontRenderer, "" + (int)potion_current_duration, 0, 0, 0xFFFFFF);
                	GL11.glScalef(1 / 0.75f, 1 / 0.75f, 1);
                	GL11.glPopMatrix();
                }
            }
            
            GL11.glScalef(rv_size, rv_size, 1);
        }
	}
	
	public void draw_region_title_to_player_entering_region()
	{
		if(!is_player_entering_region)
			return;
		
		long current_time = System.currentTimeMillis();
		String name = region_name;
		
		if(!was_player_entering_region)
		{
			last_entered_region_time = current_time;
			was_player_entering_region = true;
		}
		
		float alpha = 10;
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPushMatrix();  
		GL11.glTranslatef(screen_width / 2, screen_height / 2 - 16, 0);
		GL11.glScalef(4, 4, 1);
		/*
		if(((current_time - last_entered_region_time) / 1000) < region_name_showing_time)
		{
			float ratio = ((float)(current_time - last_entered_region_time) / 1000f) / region_name_showing_time;
			alpha = 255 * ratio;
			
			alpha = Math.max(1f, alpha);
			alpha = Math.min(255f, alpha);
			this.drawCenteredString(this.mc.fontRenderer, name, 0, 0, (int)alpha * 16777216 + 0xFFFFFF, false, 1);
		}
		else if(((current_time - last_entered_region_time) / 1000) - region_name_showing_time < region_name_staying_time)
		{
			alpha = 255;
			this.drawCenteredString(this.mc.fontRenderer, name, 0, 0, (int)alpha * 16777216 + 0xFFFFFF, false, 1);
		}
		else if(((current_time - last_entered_region_time) / 1000) - (region_name_showing_time + region_name_staying_time) < region_name_showing_time)
		{
			float ratio = (((float)(current_time - last_entered_region_time) / 1000) - (region_name_showing_time + region_name_staying_time)) / region_name_showing_time;
			float reverse_ratio = 1 - ratio;
			alpha = 255 * reverse_ratio;
			
			alpha = Math.max(1f, alpha);
			alpha = Math.min(255f, alpha);
			this.drawCenteredString(this.mc.fontRenderer, name, 0, 0, (int)alpha * 16777216 + 0xFFFFFF, false, 1);
		}
		else
		{
			is_player_entering_region = false;
			was_player_entering_region = false;
		}
		*/
		
		if((current_time - last_entered_region_time) / 1000 < region_name_showing_time)
		{
			this.drawCenteredString(this.mc.fontRenderer, name, 0, 0, 0x78FFFFFF, false, 1);
		}
		else
		{
			is_player_entering_region = false;
			was_player_entering_region = false;
		}
		
		GL11.glScalef(1 / 4, 1 / 4, 1);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

	/*
	public void in_region_name_drawing(String name, float time)
	{
		in_region_name_drawing_thread drawing_thread = new in_region_name_drawing_thread(name, time);
		if(drawing_thread.isAlive())
			drawing_thread.stop();
		drawing_thread.start();
	}
	
	class in_region_name_drawing_thread extends Thread
	{
		String region_name;
		float suspending_time;
		
		long last_time = 0;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		public in_region_name_drawing_thread(String name, float time)
		{
			region_name = name;
			suspending_time = time;
			
			last_time = System.currentTimeMillis();
		}
		
		public void run()
		{
			long current_time = System.currentTimeMillis();
			while((current_time - last_time) * 0.001f <= suspending_time)
			{
				YRSMod.debug.print("time:" + ((current_time - last_time) * 0.001f));
				ClientProxy.render_gui_screen_event_handler.get_custom_gui_ingame().drawString(Minecraft.getMinecraft().fontRenderer, "[" + region_name + "]", ((float)screen_width / 2), ((float)screen_height / 2), 0xFFFFFF, false, 2);
				
		        current_time = System.currentTimeMillis();
			}
		}
	}
	*/
	
	public void render_player_model()
	{
        int k = this.screen_width;
        int l = this.screen_height;
        RenderHelper.enableStandardItemLighting();
        
		GL11.glPushMatrix();
        GL11.glScalef(gui_scale, gui_scale, gui_scale);
        GL11.glTranslatef((k / 2) * ((1 / gui_scale) - 1), l  * ((1 / gui_scale) - 1), 0);
        
        int p0 = (this.screen_width / 2);
        int p1 = this.screen_height + 45;
        int p2 = 55;
        EntityLivingBase p3 = this.mc.thePlayer;
        
        GL11.glTranslatef((float)p0, (float)p1, -300);
        GL11.glScalef((float)(-p2), (float)p2, (float)p2);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p3.renderYawOffset;
        float f3 = p3.rotationYaw;
        float f4 = p3.rotationPitch;
        float f5 = p3.prevRotationYawHead;
        float f6 = p3.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(0, 1.0F, 1.0F, 1.0F);
        
        p3.renderYawOffset = 0;
        p3.rotationYaw = 0;
        p3.rotationPitch = 0;
        
        p3.rotationYawHead = 0;
        p3.prevRotationYawHead = 0;
        
        GL11.glTranslatef(0.0F, p3.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(p3, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        p3.renderYawOffset = f2;
        p3.rotationYaw = f3;
        p3.rotationPitch = f4;
        p3.prevRotationYawHead = f5;
        p3.rotationYawHead = f6;
                
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        
        GL11.glPopMatrix();
        
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glColor4f(1, 1, 1, 1);
	}

	public void renderInventorySlot(int p_73832_1_, float p_73832_2_, float p_73832_3_, float p_73832_4_)
    {
        ItemStack itemstack = this.mc.thePlayer.inventory.mainInventory[p_73832_1_];

        if (itemstack != null)
        {
            float f1 = (float)itemstack.animationsToGo - p_73832_4_;
            
            GL11.glPushMatrix();
            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(p_73832_2_ + 8), (float)(p_73832_3_ + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(p_73832_2_ + 8)), (float)(-(p_73832_3_ + 12)), 0.0F);
            }
            GL11.glTranslatef(p_73832_2_, p_73832_3_, 0);

            if(itemstack != null)
            	itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, 0, 0);

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
                GL11.glTranslatef(p_73832_2_, p_73832_3_, 0);
            }

            if(itemstack != null)
            	itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, 0, 0);
            
            GL11.glPopMatrix();
        }
    }

	public void render_item(ItemStack itemstack, int p_73832_2_, int p_73832_3_, float p_73832_4_)
	{
		if (itemstack != null)
        {
            float f1 = (float)itemstack.animationsToGo - p_73832_4_;

            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(p_73832_2_ + 8), (float)(p_73832_3_ + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(p_73832_2_ + 8)), (float)(-(p_73832_3_ + 12)), 0.0F);
            }
            
            try {
            	itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, p_73832_2_, p_73832_3_);
            }
            catch(Exception e) {
            	
            }

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            try {
            	itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, p_73832_2_, p_73832_3_);
            }
            catch(Exception e) {
            	
            }
        }
	}
	
	
	public void enter_in_region(String r_name)
	{
		this.region_name = r_name;
		is_player_entering_region = true;
		was_player_entering_region = false;
	}
}
