package com.abnull.yrs.file;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.minecraft.nbt.NBTTagCompound;

public abstract class FileImageBase extends FileListFilesBase {

	public FileImageBase(String name, String path) {
		super(name, path);
	}

	@Override
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
					NBTTagCompound image_data = new NBTTagCompound();
					
					String file_name = file.getName().replaceAll(".png", "");
					BufferedImage bi = ImageIO.read(file);
					int width = bi.getWidth();
					int height = bi.getHeight();
					for(int w = 0; w < width; w++)
					{
						for(int h = 0; h < height; h++)
						{
							image_data.setInteger(w + "," + h, bi.getRGB(w, h));
						}
					}
					image_data.setString("name", file_name);
					
					image_data.setInteger("width", width);
					image_data.setInteger("height", height);
					
					data.setTag(file_name, image_data);
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
}
