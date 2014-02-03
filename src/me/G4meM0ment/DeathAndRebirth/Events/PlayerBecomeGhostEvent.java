package me.G4meM0ment.DeathAndRebirth.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBecomeGhostEvent extends Event implements Cancellable {

	/**
	 * 
	 * This event fires when a player gets killed and DaR tries to
	 * execute all methods for him to become a ghost
	 * 
	 */
	
	private static final HandlerList HANDLERS = new HandlerList();
	boolean cancelled = false;
	Player player;
	Location location;
	
	public PlayerBecomeGhostEvent(Player player, Location location)
	{
		this.player = player;
		this.location = location;
	}
	
	@Override
	public boolean isCancelled() 
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) 
	{
		cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() 
	{
		return HANDLERS;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Location getLocation() 
	{
		return location;
	}
	public void setLocation(Location location) 
	{
		this.location = location;
	}
	
}
