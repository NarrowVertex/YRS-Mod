package com.abnull.yrs.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.abnull.yrs.proxy.ServerProxy;

import net.minecraft.nbt.NBTTagCompound;

public class FileJob extends FileListFilesBase {

	public FileJob()
	{
		super("job", "Jobs");
	}
	
	@Override
	public void load(NBTTagCompound data)
	{
		super.load(data);
		
		String base_job = ServerProxy.config_data.getString("base-job");
		String file_name_array = data.getString("file_name_array");
		
		if(!file_name_array.contains(base_job))
		{
			create_base_job(data, base_job);
		}
		
		NBTTagCompound job_datas = ServerProxy.job_data;
		String[] job_names = job_datas.getString("file_name_array").split(",");
		for(int n = 0; n < job_names.length; n++)
		{
			String job_name = job_names[n];
			NBTTagCompound job_data = job_datas.getCompoundTag(job_name);
			String skill_array = job_data.getString("skill");
			if(!skill_array.contains(","))
			{
				;
			}
			else
			{
				NBTTagCompound skill_tag = new NBTTagCompound();
				String[] skill_names = skill_array.split(",");
				for(int n1 = 0; n1 < skill_names.length; n1++)
				{
					String skill_name = skill_names[n1];
					NBTTagCompound skill_data = ServerProxy.skill_data.getCompoundTag(skill_name);
					skill_tag.setTag(skill_name, skill_data);
				}
				job_data.setTag("skill_data", skill_tag);
			}
		}
	}
	
	public void create_base_job(NBTTagCompound data, String base_job_name)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file_directory + "/" + base_job_name + ".txt"));
			
			write_line(bw, "name:" + base_job_name);
			write_line(bw, "hp:20");
			write_line(bw, "hp-regen-amount:0");
			write_line(bw, "hp-regen-speed:0");
			write_line(bw, "hp-level-up-equation:'hp' * (1.1^'level')");
			write_line(bw, "mp:20");
			write_line(bw, "mp-regen-amount:0");
			write_line(bw, "mp-regen-speed:0");
			write_line(bw, "mp-level-up-equation:'mp' * (1.1^'level')");
			write_line(bw, "exp:10");
			write_line(bw, "exp-level-up-equation:'exp' * (1.1^'level')");
			write_line(bw, "skill:");
			
			close(bw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NBTTagCompound base_job_data = new NBTTagCompound();
		base_job_data.setString("name", base_job_name);
		base_job_data.setString("hp", "20");
		base_job_data.setString("hp-regen-amount", "0");
		base_job_data.setString("hp-regen-speed", "0");
		base_job_data.setString("hp-level-up-equation", "'hp' * (1.1^'level')");
		base_job_data.setString("mp", "20");
		base_job_data.setString("mp-regen-amount", "0");
		base_job_data.setString("mp-regen-speed", "0");
		base_job_data.setString("mp-level-up-equation", "'mp' * (1.1^'level')");
		base_job_data.setString("exp", "10");
		base_job_data.setString("exp-level-up-equation", "'exp' * (1.1^'level')");
		base_job_data.setString("skill", "");
		
		data.setTag(base_job_name, base_job_data);
		data.setString("file_name_array", data.getString("file_name_array") + base_job_name + ",");
	}
}
