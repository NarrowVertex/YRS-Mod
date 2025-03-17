package com.abnull.yrs.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public abstract class FileBase implements IFile {

	protected final String base_path = System.getProperty("user.dir") + "/YRS Mod";
	
	public String file_name;
	public String file_path;
	
	public File file;
	public File file_directory;
	
	public FileBase(String name)
	{
		this.file_name = name;
		this.file_path = "";
		
		file_directory = new File(this.base_path);
		file = new File(this.base_path + "/" + name + ".txt");
	}
	
	public FileBase(String name, String path)
	{		
		this.file_name = name;
		this.file_path = path;
		
		file_directory = new File(this.base_path + "/" + path);
		file = new File(this.base_path + "/" + path + "/" + name + ".txt");
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
				write_line(bw, label + ":" + data.getString(label));
			}
			
			close(bw);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void load(NBTTagCompound data)
	{
		if(!file_directory.exists())
			file_directory.mkdirs();
		
		if(!file.exists())
			init();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String label_array = "";
			
			String string;
			while((string = br.readLine()) != null)
			{
				if(string.contains(":"))
				{
					String label = string.split(":", 2)[0];
					String contents = string.split(":", 2)[1];
					
					data.setString(label, contents);
					label_array += (label + ",");
				}
			}
			
			close(br);
			data.setString("label_array", label_array);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write_line(BufferedWriter bw, String message) throws IOException
	{
		bw.write(message);
		bw.newLine();
	}
	
	public void close(Closeable buff) throws IOException
	{
		buff.close();
	}
}
