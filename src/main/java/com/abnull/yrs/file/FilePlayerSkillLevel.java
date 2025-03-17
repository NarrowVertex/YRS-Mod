package com.abnull.yrs.file;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.abnull.yrs.proxy.ServerProxy;

import net.minecraft.nbt.NBTTagCompound;

public class FilePlayerSkillLevel extends FilePlayerBase {

	public FilePlayerSkillLevel()
	{
		super("PlayerSkillLevel");
	}

	@Override
	public void init()
	{
		String[] skill_name_array = ServerProxy.skill_data.getString("file_name_array").split(",");
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			for(int n = 0; n < skill_name_array.length; n++)
			{
				String skill_name = skill_name_array[n];
				write_line(bw, skill_name + ":" + 0);
			}
			
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void load(NBTTagCompound data)
	{
		super.load(data);
		String label_array = data.getString("label_array");
		
		NBTTagCompound skill_data = ServerProxy.skill_data;
		String[] skill_name_array = skill_data.getString("file_name_array").split(",");
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			for(int n = 0; n < skill_name_array.length; n++)
			{
				String skill_name = skill_name_array[n];
				if(!label_array.contains(skill_name))
				{
					write_line(bw, skill_name + ":" + 0);
					data.setString(skill_name, "0");
					data.setString("label_array", data.getString("label_array") + skill_name + ",");
				}
				else
				{
					write_line(bw, skill_name + ":" + data.getString(skill_name));
				}
			}
			
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
