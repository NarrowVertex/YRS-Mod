package com.abnull.yrs.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.IOUtils;

import com.abnull.yrs.YRSMod;

import net.minecraft.nbt.NBTTagCompound;

public abstract class FileSoundBase extends FileListFilesBase {

	final int byte_interval = 1024;
	
	public FileSoundBase(String name, String path)
	{
		super(name, path);
	}
	
	@Override
	public void load_directory_files(File directory, NBTTagCompound data)
	{
		if(!directory.exists())
			directory.mkdirs();
		
		File[] list_files = directory.listFiles();
		
		try {
			String sound_name_array = "";
			for(int n = 0; n < list_files.length; n++)
			{
				File file = list_files[n];
				
				if(!file.isDirectory())
				{
					NBTTagCompound sound_data = new NBTTagCompound();
					
					String file_name = file.getName().replaceAll(".mp3", "");
					InputStream is = new FileInputStream(file);
					
					BufferedInputStream bis = new BufferedInputStream(is);
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nRead;
					byte[] bytes = new byte[bis.available()];
					while((nRead = bis.read(bytes, 0, bytes.length)) != -1) {
						buffer.write(bytes, 0, nRead);
					}
					
					buffer.flush();
					bis.close();
					is.close();
					
					int count = bytes.length / byte_interval;
					int left_count = bytes.length - count * byte_interval;
					
					for(int m = 0; m < count + 1; m++)
					{
						int byte_array_length = ((m != count) ? byte_interval : left_count);
						byte[] byte_array = new byte[byte_array_length];
						for(int o = 0; o < byte_array_length; o++)
						{
							byte b = bytes[m * byte_interval + o];
							byte_array[o] = b;
						}
						sound_data.setByteArray("" + m, byte_array);
					}
					sound_data.setInteger("count", count + 1);
					sound_data.setInteger("byte_interval", byte_interval);
					
					sound_data.setString("name", file_name);
					
					data.setTag(file_name, sound_data);
					sound_name_array += file_name + ",";
				}
				else
				{
					load_directory_files(file, data);
				}
			}
			data.setString("sound_name_array", sound_name_array);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
