package me.G4meM0ment.DeathAndRebirth.Listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GraveHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.PermHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;

public class PListener implements Listener {
	
	private DeathAndRebirth subplugin;
	private GhostHandler gH;
	private GraveHandler graveH;
	private ShrineHandler sH;
	private PermHandler permH;
	private ConfigHandler cH;
	
	public PListener(DeathAndRebirth subplugin)
	{
		this.subplugin = subplugin;
		gH = new GhostHandler(subplugin);
		graveH = new GraveHandler();
		sH = new ShrineHandler();
		permH = new PermHandler();
		cH = new ConfigHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(!cH.isDAREnabled(event.getEntity().getWorld())) return;
		
		Player p = event.getEntity();
		if(p == null) return;
		
		if(permH.hasIgnorePerm(p)) return;
		/*
		 * TODO add config option
		 * if(p.getLastDamageCause().toString().equalsIgnoreCase("void")) return;
		 * 
		 */
		//Some plugins avoid death by increasing health
		if(p.getHealth() > 0) return;
		
		DARPlayer darP = gH.getDARPlayer(p);
		if(darP == null) return;
		
		if(permH.hasNoDropPerm(p) || !cH.getDropItems())
			event.getDrops().clear();
		
		gH.died(p, p.getLocation());
	}
	
	/*
	 * Check for respawning and teleport the ghost to its given location
	 * 
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(!cH.isDAREnabled(event.getPlayer().getLocation().getWorld())) return;
		
		Player p = event.getPlayer();
		if(p == null) return;
		
		if(permH.hasIgnorePerm(p)) return;
		
		DARPlayer darP = gH.getDARPlayer(p);
		if(darP == null) return;
		
		//Set location where the ghost spawns
		Location respawnLoc = gH.getRespawnLocation(darP);
		if(respawnLoc == null)
			event.setRespawnLocation(p.getLocation());
		else
			event.setRespawnLocation(respawnLoc);
		
		gH.respawned(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		/*
		 * ^
		 * TODO check for ghost interactions
		 * 
		 */
		if(!cH.isDAREnabled(event.getClickedBlock().getWorld())) return;
		
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p);
		if(darP == null) return;
		
		if(graveH.isPlayersGrave(darP, event.getClickedBlock().getLocation()))
			gH.resurrected(p, event.getClickedBlock().getLocation());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(!cH.isDAREnabled(event.getPlayer().getWorld())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		
		if(gH.getDARPlayer(p).isDead())
			event.setCancelled(true);
	}
}
