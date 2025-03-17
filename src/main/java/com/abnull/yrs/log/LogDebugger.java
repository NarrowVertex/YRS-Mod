package com.abnull.yrs.log;

public class LogDebugger {

	String log_tag;
	boolean is_client;
	
	public LogDebugger(boolean is_client) 
	{
		log_tag = is_client ? "[YRS Client]" : "[YRS Server]";
		this.is_client = is_client;
	}
	
	public void print(String content) 
	{
		System.out.println(log_tag + " " + content);
	}
	
	public void print_debug(String class_name, int line_num, String content)
	{
		print(class_name + "(" + line_num + ") : " + content);
	}
	
	public void print_error(String class_name, int line_num, int error_code, String error_content) 
	{
		print("#ERROR# (" + class_name + "(" + line_num + "), " + error_code + ") : " + error_content);
	}
}
