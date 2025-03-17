package com.abnull.yrs.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

import com.abnull.yrs.proxy.ServerProxy;

import cpw.mods.fml.common.network.ByteBufUtils;

public class FilePlayerPotionTree extends FilePlayerBase {

	public FilePlayerPotionTree()
	{
		super("PlayerPotionTree");
	}

	@Override
	public void init()
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "0:n");
			write_line(bw, "1:n");
			write_line(bw, "2:n");
			
			close(bw);
		} catch (IOException e) {
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
					
					NBTTagCompound potion_item_data = new NBTTagCompound();
					if(!contents.equals("n"))
					{
						String[] array = contents.split(" ");
						
						NBTTagCompound item_stack_data = new NBTTagCompound();
						byte[] bytes = new byte[array.length];
						for(int n = 0; n < bytes.length; n++)
						{
							bytes[n] = Byte.parseByte(array[n]);
						}
						ByteBuf bytebuf = Unpooled.copiedBuffer(bytes);
						item_stack_data = ByteBufUtils.readTag(bytebuf);
						
						potion_item_data.setTag("data", item_stack_data);
						potion_item_data.setBoolean("is_null", false);
					}
					else
					{
						potion_item_data.setBoolean("is_null", true);
					}
					data.setTag(label, potion_item_data);
				}
			}
			
			close(br);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
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
			
			for(int n = 0; n < 3; n++)
			{
				NBTTagCompound potion_data = data.getCompoundTag("" + n);
				boolean is_null = potion_data.getBoolean("is_null");
				
				if(!is_null)
				{
					NBTTagCompound item_stack_data = potion_data.getCompoundTag("data");
					ByteBuf bytebuf = Unpooled.buffer();
					ByteBufUtils.writeTag(bytebuf, item_stack_data);
					byte[] bytes = new byte[bytebuf.readableBytes()];
					bytebuf.getBytes(bytebuf.readerIndex(), bytes);
					
					String array = "";
					for(int num = 0; num < bytes.length; num++)
					{
						array += bytes[num] + " ";
					}
					write_line(bw, n + ":" + array);
				}
				else
				{
					write_line(bw, n + ":" + "n");
				}
			}
			
			close(bw);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
