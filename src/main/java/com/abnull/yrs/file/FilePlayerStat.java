package com.abnull.yrs.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.abnull.yrs.proxy.ServerProxy;

public class FilePlayerStat extends FilePlayerBase {

	public FilePlayerStat() {
		super("PlayerStat");
	}

	@Override
	public void init() {		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			write_line(bw, "str:0");
			write_line(bw, "def:0");
			write_line(bw, "acc:0");
			write_line(bw, "agi:0");
			
			close(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
