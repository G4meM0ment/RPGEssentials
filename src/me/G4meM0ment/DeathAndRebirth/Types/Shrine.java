package me.G4meM0ment.DeathAndRebirth.Types;

import org.bukkit.Location;

public class Shrine {
	
	private Location p1, p2, spawn;
	private boolean binding;
	private String name;
	
	public Location getP1() 
	{
		return p1;
	}
	public void setP1(Location p1) 
	{
		this.p1 = p1;
	}
	
	public Location getP2() 
	{
		return p2;
	}
	public void setP2(Location p2) 
	{
		this.p2 = p2;
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
