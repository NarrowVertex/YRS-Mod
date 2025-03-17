package com.abnull.yrs.region;

import java.util.ArrayList;
import java.util.List;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.util.Point;

import net.minecraft.entity.player.EntityPlayer;

public class Region {

	public String region_name;
	public List<Point> point_list = new ArrayList<Point>();
	
	public Region(String name, List<Point> points)
	{
		this.region_name = name;
		this.point_list = points;
	}
	
	public boolean is_player_in_region(EntityPlayer player)
	{
		double player_x = player.posX;
		double player_y = player.posZ;
		
		int over_xline_num = 0;
		int under_xline_num = 0;
		
		int over_yline_num = 0;
		int under_yline_num = 0;
		
		for(int n = 0, m = 1; n < point_list.size(); n++, m++)
		{
			if(m == point_list.size())
				m = 0;
			
			Point p1 = point_list.get(n);
			Point p2 = point_list.get(m);
			
			double slope_value = (p2.y - p1.y) / (p2.x - p1.x);
			double re_slope_value = (p2.x - p1.x) / (p2.y - p1.y);
			
			if(min(p1.x, p2.x) <= player_x && player_x < max(p1.x, p2.x))
			{
				double y_value = slope_value * (player_x - p1.x) + p1.y;
				
				if(y_value <= player_y)
					under_yline_num++;
				else if(player_y <= y_value)
					over_yline_num++;
			}
			
			if(min(p1.y, p2.y) <= player_y && player_y < max(p1.y, p2.y))
			{
				double x_value = p1.x + re_slope_value * (player_y - p1.y);
				
				if(x_value <= player_x)
					under_xline_num++;
				else if(player_x <= x_value)
					over_xline_num++;
			}
		}
		if(!(over_xline_num % 2 == 0) && !(under_xline_num % 2 == 0))
		{
			if(!(over_yline_num % 2 == 0) && !(under_yline_num % 2 == 0))
			{
				return true;
			}
		}
		return false;
	}
	
	public String get_region_name()
	{
		return region_name;
	}
	
	double min(double a, double b)
	{
		return Math.min(a, b);
	}
	
	double max(double a, double b)
	{
		return Math.max(a, b);
	}
}