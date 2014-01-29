package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.Types.Shrine;

import org.bukkit.Location;

public class ShrineHandler {
	
	private ConfigHandler cH;
	
	private static List<Shrine> shrines = new ArrayList<Shrine>();
	
	public ShrineHandler()
	{
		cH = new ConfigHandler();
	}
	
	public List<Shrine> getShrines()
	{
		return shrines;
	}

	public Shrine getNearestShrine(Location location) 
	{
		if(location == null) return null;
		if(!cH.isDAREnabled(location.getWorld())) return null;
		
		Shrine nearest = null;		
		for(Shrine s : getShrines())
		{
			if(nearest == null)
				nearest = s; 
			if(s.getSpawn().distance(location) < nearest.getSpawn().distance(location))
				nearest = s;
		}
		return nearest;
	}
	
	public Shrine getShrine(String name)
	{
		for(Shrine s : getShrines())
			if(s.getName().equals(name))
				return s;
		return null;
	}
}
