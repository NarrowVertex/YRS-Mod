package com.abnull.yrs.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.abnull.yrs.proxy.ClientProxy;

import javazoom.jl.player.Player;
 
public class SoundHelper {

	static Player player;
	
	public static void play_sound(String sound_name) 
	{
		if(ClientProxy.sound_data_map.containsKey(sound_name))
		{
			System.out.println("Couldn't find that sound file. . .(" + sound_name + ")");
			return;
		}
		
		byte[] sound_file_data = ClientProxy.sound_data_map.get(sound_name);
		if(sound_file_data != null && sound_file_data.length != 0)
		{
			InputStream is = new ByteArrayInputStream(sound_file_data);
			BufferedInputStream bis = new BufferedInputStream(is);
			
			try {
				if(player != null)
					if(!player.isComplete())
						player.close();
				
				player = new Player(bis);
				player.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
