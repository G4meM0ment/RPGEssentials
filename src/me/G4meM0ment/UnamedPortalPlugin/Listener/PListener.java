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

public class PListener implements Listener{
	
	private PortalHandler ph;
	private PermHandler pm;
	
	public PListener(RPGEssentials plugin) {
		ph = new PortalHandler(new UnnamedPortalPlugin());
		pm = new PermHandler(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		Block b = event.getPlayer().getLocation().getBlock();
		if(p == null || b == null) return;
		if(ph.isPortal(b) && pm.hasPortalPerm(p, ph.getPortal(b))) {
			ph.accessedPortal(p, ph.getPortal(b));
		}	
	}
}
