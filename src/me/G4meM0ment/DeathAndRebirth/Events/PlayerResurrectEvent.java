package me.G4meM0ment.DeathAndRebirth.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerResurrectEvent extends Event implements Cancellable {

	/**
	 * 
	 * This event fires when a player right clicks his grave or shrine or another player
	 * resurrects him (however he does)
	 * 
	 */
	
	
	private static final HandlerList HANDLERS = new HandlerList();
	boolean cancelled = false;
	Player resurrected, resurrecter;
	Location clicked, reborn;
	
	public PlayerResurrectEvent(Player resurrected, Player resurrecter, Location loc)
	{
		this.resurrected = resurrected;
		this.resurrecter = resurrecter;
		this.clicked = loc;
	}

	@Override
	public boolean isCancelled() 
	{
		return false;
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
	
	/**
	 * 
	 * Params getters and setters
	 * @return
	 */
	public Player getResurrected() 
	{
		return resurrected;
	}
	public void setResurrected(Player resurrected) 
	{
		this.resurrected = resurrected;
	}

	public Player getResurrecter() 
	{
		return resurrecter;
	}
	public void setResurrecter(Player resurrecter) 
	{
		this.resurrecter = resurrecter;
	}

	public Location getClicked() 
	{
		return clicked;
	}
	public void setClicked(Location clicked) 
	{
		this.clicked = clicked;
	}

}
