package me.G4meM0ment.UnamedPortalPlugin.Event;

import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PortalEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
	private Player teleportee;
    private Portal portal;
    private boolean isCancelled;
    
    public PortalEvent(Portal portal, Player teleportee) 
    {
    	this.portal = portal;
    	this.teleportee = teleportee;
    }
    
    public Player getTeleportee() {
    	return teleportee;
    }
    public void setTeleportee(Player teleportee)
    {
    	this.teleportee = teleportee;
    }
    
    public Portal getPortal() {
    	return portal;
    }
    public void setPortal(Portal portal) 
    {
    	this.portal = portal;
    }
    
	@Override
	public boolean isCancelled() 
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) 
	{
		this.isCancelled = cancelled;		
	}

	@Override
	public HandlerList getHandlers() 
	{
		return HANDLERS;
	}
	public static HandlerList getHandlerList() 
	{
	    return HANDLERS;
	}

}
