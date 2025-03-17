package com.abnull.yrs.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.abnull.yrs.proxy.ServerProxy;

public class FilePlayerData extends FilePlayerBase {

	public FilePlayerData() {
		super("PlayerData");
	}

	public void init()
	{
		String base_job_name = ServerProxy.config_data.getString("base-job");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "max-hp:50");
			write_line(bw, "hp:50");
			
			write_line(bw, "hp-regen-speed:1");
			write_line(bw, "hp-regen-amount:1");
			
			write_line(bw, "max-mp:50");
			write_line(bw, "mp:50");
			
			write_line(bw, "mp-regen-speed:1");
			write_line(bw, "mp-regen-amount:1");
			
			write_line(bw, "exp-level:0");
			write_line(bw, "max-exp:" + ServerProxy.job_data.getCompoundTag(base_job_name).getString("exp"));
			write_line(bw, "exp:0");
			
			write_line(bw, "skill-point:0");
			write_line(bw, "stat-point:0");
			
			write_line(bw, "region:None");
			
			write_line(bw, "jobs:" + base_job_name + ";");
			
			close(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void load(NBTTagCompound data)
	{
		super.load(data);
		ServerProxy.player_data_manager.load_player_data(player);
	}
	
	@Override
	public void save(NBTTagCompound data)
	{		
		if(!file_directory.exists())
		{
			file_directory.mkdirs();
			init();
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			String[] label_array = data.getString("label_array").split(",");
			for(int n = 0; n < label_array.length; n++)
			{
				String label = label_array[n];
				
				if(label.equals("hp"))
					write_line(bw, label + ":" + player.getHealth());
				else if(label.equals("max-hp"))
					write_line(bw, label + ":" + player.getMaxHealth());
				else
					write_line(bw, label + ":" + data.getString(label));
			}
			
			close(bw);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		ServerProxy.player_data_manager.save_player_data(player);
	}
}
