package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.Types.Shrine;

import org.bukkit.Location;
import org.bukkit.World;

public class ShrineHandler {
	
	private static HashMap<String, List<Shrine>> shrines = new HashMap<String, List<Shrine>>();
	
	/**
	 * Returns the map containing all worlds lists with shrines
	 * @return
	 */
	public HashMap<String, List<Shrine>> getShrineLists()
	{
		return shrines;
	}
	/**
	 * Returns the list of shrines for the given world
	 * @param worldName
	 * @return
	 */
	public List<Shrine> getShrines(String worldName)
	{
		return getShrineLists().get(worldName);
	}
	/**
	 * for each enabled world a new list is created
	 */
	public void initShrineLists()
	{
		for(String w : ConfigHandler.enabledWorlds)
		{
			if(w == null) continue;
			getShrineLists().put(w, new ArrayList<Shrine>());
		}
	}
	
	/**
	 * Add a new shrine to a worlds list, the boolean to check if it has worked
	 * @param name
	 * @param p1
	 * @param p2
	 * @param spawn
	 * @param binding
	 * @return
	 */
	public boolean addShrine(String name, Location p1, Location p2, Location spawn, boolean binding)
	{
		if(name == null || p1 == null || p2 == null) return false;
		if(!ConfigHandler.isDAREnabled(p1.getWorld().getName())) return false;
		if(!getShrineLists().containsKey(p1.getWorld().getName()))
			getShrineLists().put(p1.getWorld().getName(), new ArrayList<Shrine>());
		
		//sort the points after there max and minimal coord value
		int maxX, maxY, maxZ, minX, minY, minZ, p1X, p1Y, p1Z, p2X, p2Y, p2Z;
		p1X = p1.getBlockX(); p1Y = p1.getBlockY(); p1Z = p1.getBlockZ();
		p2X = p2.getBlockX(); p2Y = p2.getBlockY(); p2Z = p2.getBlockZ();
		
/*		if(p1X > p2X) {maxX = p1X; minX = p2X;} else {maxX = p2X; minX = p1X;}
		if(p1Y > p2Y) {maxY = p1Y; minY = p2Y;} else {maxY = p2Y; minY = p1Y;}
		if(p1Z > p2Z) {maxZ = p1Z; minZ = p2Z;} else {maxZ = p2Z; minZ = p1Z;} */
		
		if(p1X<p2X) { minX = p1X; maxX = p2X; } else { minX = p2X; maxX = p1X;	}
		if(p1Y<p2Y) { minY = p1Y; maxY = p2Y; } else { minY = p2Y; maxY = p1Y;	}
		if(p1Z<p2Z) { minZ = p1Z; maxZ = p2Z; } else { minZ = p2Z; maxZ = p1Z;	}
		
		//create new locations of these sorted values (it's a cuboid this will work everytime ;))
		Location max = new Location(p1.getWorld(), maxX, maxY, maxZ);
		Location min = new Location(p1.getWorld(), minX, minY, minZ);
		
		//add a new shrine to the worlds list (world defined by one of the locations)
		getShrines(p1.getWorld().getName()).add(new Shrine(name, max, min, spawn, binding));
		return true;
	}
	/**
	 * remove a shrine from cache
	 * @param s
	 * @param world
	 */
	public void removeShrine(Shrine s, World world)
	{
		getShrines(world.getName()).remove(s);
	}

	/**
	 * Returns nearest shrine spawn location, shrine itself can actually be else where
	 * @param location
	 * @return
	 */
	public Shrine getNearestShrineSpawn(Location location) 
	{
		if(location == null) return null;
		if(!ConfigHandler.isDAREnabled(location.getWorld().getName())) return null;
		
		//check every shrine for its distance to the given location
		Shrine nearest = null;		
		for(Shrine s : getShrines(location.getWorld().getName()))
		{
			//no shrine added yet
			if(nearest == null)
				nearest = s; 
			//if distance is lower it's the new nearest
			if(s.getSpawn().distance(location) < nearest.getSpawn().distance(location))
				nearest = s;
		}
		return nearest;
	}
	
	/**
	 * Checks if location (plus the radius around) is the given shrine
	 * @param s
	 * @param loc
	 * @param radius
	 * @return
	 */
	public boolean isShrine(Shrine s, Location loc, int radius)
	{
		if(s == null) return false;
		if(loc == null) return false;
		
		if(getShrine(loc, radius) != null)
			return true;
		return false;
	}
	/**
	 * Checks if the location (plus the radius around) is a shrine
	 * @param loc
	 * @param radius
	 * @return
	 */
	public boolean isShrine(Location loc, int radius)
	{
		if(loc == null) return false;
		
		for(Shrine s : getShrines(loc.getWorld().getName()))
			if(isShrine(s, loc, radius))
				return true;
		return false;
	}
	
	/**
	 * Gets a shrine by its name and world
	 * @param name
	 * @param world
	 * @return
	 */
	public Shrine getShrine(String name, World world)
	{
		for(Shrine s : getShrines(world.getName()))
			if(s.getName().equals(name))
				return s;
		return null;
	}
	/**
	 * Get the Shrine which exists at this location
	 * @param loc
	 * @return
	 */
	public Shrine getShrine(Location loc, int radius)
	{		
		for(Shrine s : getShrines(loc.getWorld().getName()))
		{
			int locX = loc.getBlockX(),
				locY = loc.getBlockY(),
				locZ = loc.getBlockZ(),
				maxX = s.getMax().getBlockX(),
				maxY = s.getMax().getBlockY(),
				maxZ = s.getMax().getBlockZ(),
				minX = s.getMin().getBlockX(),
				minY = s.getMin().getBlockY(),
				minZ = s.getMin().getBlockZ();
				    										
			if(     locX <= maxX+radius
				&&	locX >= minX-radius
				&&	locY <= maxY+radius
				&&	locY >= minY-radius
				&&	locZ <= maxZ+radius
				&&	locZ >= minZ-radius)
				return s;
		}
		return null;
	}
}
