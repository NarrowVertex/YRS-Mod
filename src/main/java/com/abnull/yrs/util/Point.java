package com.abnull.yrs.util;

public class Point {
	
	public double x;
	public double y;
	
	public Point() 
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Point(String array)
	{
		String[] point_array = array.split(",");
		try {
			this.x = Double.parseDouble(point_array[0]);
			this.y = Double.parseDouble(point_array[1]);
		}
		catch(Exception e){
			return;
		}
	}
}
