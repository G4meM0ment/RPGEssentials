package me.G4meM0ment.DeathAndRebirth.Listener;

import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

public class EListener implements Listener {

	private GhostHandler gH;
	private ShrineHandler sH;
	
	public EListener()
	{
		gH = new GhostHandler();
		sH = new ShrineHandler();
	}
	
	/**
	 * Checks for ghost being attacked
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event)
	{	
		if(!(event.getEntity() instanceof Player)) return;
		DARPlayer darP = gH.getDARPlayer((Player) event.getEntity(), event.getEntity().getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
		{
			event.setCancelled(true);
		
			/*
			 * Teleport player out of void to avoid death while being a ghost try-catch to avoid NPEs when reason is null, can be on /kill
			 */
			try
			{
				if(event.getCause().equals(DamageCause.VOID))
					if(ConfigHandler.corpseSpawn)
						darP.getPlayer().teleport(darP.getGrave().getLocation());
					else
						darP.getPlayer().teleport(sH.getNearestShrineSpawn(event.getEntity().getLocation()).getSpawn());
			}
			catch(NullPointerException e){}
		}
	}
	
	/**
	 * Checks for ghosts attacking
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{	
		if(!(event.getDamager() instanceof Player)) return;
		DARPlayer darP = gH.getDARPlayer((Player) event.getDamager(), event.getDamager().getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
		{
			event.setCancelled(true);
			darP.getPlayer().sendMessage("You can't do that while being a ghost");
		}
	}
	
	/**
	 * Checks for monsters attacking ghosts
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent event)
	{	
		if(!(event.getTarget() instanceof Player)) return;
		DARPlayer darP = gH.getDARPlayer((Player) event.getTarget(), event.getTarget().getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
			event.setCancelled(true);
	}
}
