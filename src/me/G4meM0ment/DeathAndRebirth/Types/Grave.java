package me.G4meM0ment.DeathAndRebirth.Types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class Grave {
	
	private Location loc;
	private Sign sign;
	private String player;
	private long placedMillis;
	private Material blockMaterial;
	private int data;

	public Grave(Location loc, Sign sign, String p, long placedMillis)
	{
		this.loc = loc;
		this.sign = sign;
		this.player = p;
		this.placedMillis = placedMillis;
	}
	
	public Location getLocation() 
	{
		return loc;
	}
	public void setLocation(Location loc) 
	{
		this.loc = loc;
	}
	
	public Sign getSign() 
	{
		return sign;
	}
	public void setSign(Sign sign) 
	{
		this.sign = sign;
	}
	
	public String getPlayerName() 
	{
		return player;
	}
	public void setPlayerName(String p) 
	{
		this.player = p;
	}
	public Player getPlayer()
	{
		return Bukkit.getPlayer(player);
	}
	
	public long getPlacedMillis() 
	{
		return placedMillis;
	}
	public void setPlacedMillis(long placedMillis) 
	{
		this.placedMillis = placedMillis;
	}
	
	public Material getBlockMaterial() 
	{
		return blockMaterial;
	}
	public void setBlockMaterial(Material blockMaterial) 
	{
		this.blockMaterial = blockMaterial;
	}

	public int getData() 
	{
		return data;
	}
	public void setData(int data) 
	{
		this.data = data;
	}	
}
