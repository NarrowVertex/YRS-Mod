package com.abnull.yrs.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.abnull.yrs.YRSMod;
import com.abnull.yrs.util.Point;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class RegionManager {
	
	private HashMap<String, Region> region_list;
	
	public void init(NBTTagCompound data)
	{
		region_list = new HashMap<String, Region>();
		String[] file_name_array = data.getString("file_name_array").split(",");
		for(String name : file_name_array)
		{
			NBTTagCompound region_data = data.getCompoundTag(name);
			NBTTagCompound region_point_data = region_data.getCompoundTag("point");
			List<Point> point_list = new ArrayList<Point>();
			for(int n = 0; n < region_point_data.getInteger("tag_contents_count"); n++) 
			{
				Point point = new Point(region_point_data.getString("" + n));
				point_list.add(point);
			}
			Region region = new Region(name, point_list);
			region_list.put(name, region);
		}
	}
	
	public void add_region(Region region)
	{
		region_list.put(region.get_region_name(), region);
	}
	
	public Region get_region(String name)
	{
		return region_list.get(name);
	}
	
	public Iterator<Region> get_region_iterator()
	{
		 return region_list.values().iterator();
	}
}
	
