package com.abnull.yrs.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class FileConfig extends FileBase {

	public FileConfig() 
	{
		super("Config");
	}
	
	public void init()
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "base-job:BaseJob");
			
			close(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
