package com.abnull.yrs.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileStatConfig extends FileBase {

	public FileStatConfig()
	{
		super("StatConfig");
	}

	public void init()
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "str-strength:1.0");
			write_line(bw, "def-defence:0.01");
			write_line(bw, "dex-probability:0.01");
			write_line(bw, "dex-critical-multiple:1.0");
			write_line(bw, "agi-probability:0.01");
			write_line(bw, "acc-accuracy:0.01");
			
			close(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
