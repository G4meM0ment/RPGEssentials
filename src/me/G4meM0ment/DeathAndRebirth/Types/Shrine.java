package me.G4meM0ment.DeathAndRebirth.Types;

import org.bukkit.Location;

public class Shrine {
	
	private Location max, min, spawn;
	private boolean binding;
	private String name;
	
	/**
	 * Contains all information needed for a shrine
	 * @param name
	 * @param max
	 * @param min
	 * @param spawn
	 * @param binding
	 */
	public Shrine(String name, Location max, Location min, Location spawn, boolean binding)
	{
		this.name = name;
		this.max = max;
		this.min = min;
		this.spawn = spawn;
		this.binding = binding;
	}
	
	public Location getMax() 
	{
		return max;
	}
	public void setMax(Location max) 
	{
		this.max = max;
	}
	
	public Location getMin() 
	{
		return min;
	}
	public void setMin(Location min) 
	{
		this.min = min;
	}
	
	public Location getSpawn() 
	{
		return spawn;
	}
	public void setSpawn(Location spawn) 
	{
		this.spawn = spawn;
	}
	
	public boolean hasBinding() 
	{
		return binding;
	}
	public void setBinding(boolean binding) 
	{
		this.binding = binding;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
