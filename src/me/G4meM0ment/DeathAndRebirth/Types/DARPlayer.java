package me.G4meM0ment.DeathAndRebirth.Types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DARPlayer {
	
	private String displayName;
	private Grave grave;
	private Shrine shrine;
	private boolean robbed, canRes, dead;
	private String player;
	
	/**
	 * Contains all information needed for a ghost (DARPlayer)
	 * @param player
	 * @param dead
	 * @param canRes
	 * @param robbed
	 * @param grave
	 * @param shrine
	 */
	public DARPlayer(String player, boolean dead, boolean canRes, boolean robbed, Grave grave, Shrine shrine)
	{
		this.player = player;
		this.dead = dead;
		this.canRes = canRes;
		this.robbed = robbed;
		this.grave = grave;
		this.shrine = shrine;
	}
	
	public String getDisplayName() 
	{
		return displayName;
	}
	public void setName(String name) 
	{
		this.displayName = name;
	}
	
	public Grave getGrave() 
	{
		return grave;
	}
	public void setGrave(Grave grave) 
	{
		this.grave = grave;
	}
	
	public Shrine getShrine() 
	{
		return shrine;
	}
	public void setShrine(Shrine shrine) 
	{
		this.shrine = shrine;
	}
	
	public boolean isRobbed()
	{
		return robbed;
	}
	public void setRobbed(boolean robbed)
	{
		this.robbed = robbed;
	}
	
	public boolean canRes() 
	{
		return canRes;
	}
	public void setCanRes(boolean canRes) 
	{
		this.canRes = canRes;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	public void setDead(boolean dead) 
	{
		this.dead = dead;
	}
	
	public String getPlayerName() 
	{
		return player;
	}
	public void setPlayerName(String player)
	{
		this.player = player;
	}
	public Player getPlayer() 
	{
		return Bukkit.getPlayer(player);
	}

}
