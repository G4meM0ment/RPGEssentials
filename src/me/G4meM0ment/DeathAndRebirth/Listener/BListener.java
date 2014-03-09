package me.G4meM0ment.DeathAndRebirth.Listener;

import me.G4meM0ment.DeathAndRebirth.Framework.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BListener implements Listener {

	private GhostHandler gH;
	private ShrineHandler sH;
	
	public BListener()
	{
		gH = new GhostHandler();
		sH = new ShrineHandler();
	}
	
	/**
	 * Prevent shrine destroying or ghosts breaking blocks
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(!(event.getPlayer() instanceof Player)) return;
		DARPlayer darP = gH.getDARPlayer((Player) event.getPlayer(), event.getPlayer().getWorld().getName());
		if(darP == null) return;
		
		if(sH.isShrine(event.getBlock().getLocation(), 0) && ConfigHandler.protectShrines)
			event.setCancelled(true);
		
		if(darP.isDead() && ConfigHandler.destroyBlocks)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("You can't destroy blocks while being a ghost");
		}
	}
	
	/**
	 * Prevent shrine building and ghosts building blocks
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(!(event.getPlayer() instanceof Player)) return;
		DARPlayer darP = gH.getDARPlayer((Player) event.getPlayer(), event.getPlayer().getWorld().getName());
		if(darP == null) return;
		
		if(sH.isShrine(event.getBlock().getLocation(), 0) && ConfigHandler.protectShrines)
			event.setCancelled(true);
		
		if(darP.isDead())
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("You can't place blocks while being a ghost");
		}
	}
}
