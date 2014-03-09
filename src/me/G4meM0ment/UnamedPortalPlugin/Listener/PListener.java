package me.G4meM0ment.UnamedPortalPlugin.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PermHandler;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class PListener implements Listener{
	
	private UnnamedPortalPlugin subplugin;
	private PortalHandler ph;
	private PermHandler pm;
	
	public PListener(RPGEssentials plugin) {
		subplugin = new UnnamedPortalPlugin();
		ph = new PortalHandler(new UnnamedPortalPlugin());
		pm = new PermHandler(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		Block b = event.getPlayer().getLocation().getBlock();
		if(p == null || b == null) return;
		if(ph.isPortal(event.getTo()) && pm.hasPortalPerm(p, ph.getPortal(event.getTo())) && !ph.hasToWait(p)) 
			ph.accessedPortal(p, ph.getPortal(event.getTo()));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPortal(PlayerPortalEvent event) {
		if(subplugin.getConfig().getBoolean("DisableNetherPortals"))
			event.setCancelled(true);
	}
}
