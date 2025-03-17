package com.abnull.yrs.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.abnull.yrs.proxy.ServerProxy;

public class FilePlayerSkillSlot extends FilePlayerBase {

	public FilePlayerSkillSlot()
	{
		super("PlayerSkillSlot");
	}

	public void init()
	{		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "slot1:n");
			write_line(bw, "slot2:n");
			write_line(bw, "slot3:n");
			write_line(bw, "slot4:n");
			write_line(bw, "slot5:n");
			write_line(bw, "slot6:n");
			
			close(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
