package com.abnull.yrs.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.minecraft.nbt.NBTTagCompound;

public abstract class FileListFilesBase extends FileBase {

	public FileListFilesBase(String name, String path) {
		super(name, path);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void load(NBTTagCompound data)
	{		
		if(!file_directory.exists())
			file_directory.mkdirs();
		
		data.setString("file_name_array", "");
		load_directory_files(file_directory, data);
	}
	
	public void load_directory_files(File directory, NBTTagCompound data)
	{
		if(!directory.exists())
			directory.mkdirs();
		
		File[] list_files = directory.listFiles();
		
		try {
			for(int n = 0; n < list_files.length; n++)
			{
				File file = list_files[n];
				
				if(!file.isDirectory())
				{
					BufferedReader br = new BufferedReader(new FileReader(file));
					NBTTagCompound file_data = new NBTTagCompound();
					
					String string, file_name = "";
					
					boolean is_tag = false;
					String tag_name = "";
					NBTTagCompound tag = null;
					int tag_contents_count = 0;
					
					while((string = br.readLine()) != null)
					{
						if(string.contains("<"))
						{
							is_tag = true;
							tag_name = string.replaceAll("<", "");
							tag = new NBTTagCompound();
							tag_contents_count = 0;
						}
						else if(string.contains("-") && is_tag)
						{
							String contents = string.replaceAll("-", "");
							tag.setString("" + tag_contents_count, contents);
							tag_contents_count++;
						}
						else if(string.contains(">"))
						{
							is_tag = false;
							tag.setInteger("tag_contents_count", tag_contents_count);
							file_data.setTag(tag_name, tag);
						}
						else if(string.contains(":") && !is_tag)
						{
							String label = string.split(":", 2)[0];
							String contents = string.split(":", 2)[1];
							
							if(label.equalsIgnoreCase("name"))
							{
								file_name = contents;
							}
							
							file_data.setString(label, contents);
						}
					}
					br.close();
					data.setTag(file_name, file_data);
					data.setString("file_name_array", data.getString("file_name_array") + file_name + ",");
				}
				else
				{
					load_directory_files(file, data);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void save(NBTTagCompound data)
	{
		
	}
}
