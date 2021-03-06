package me.G4meM0ment.ReNature.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.ReNature;
import me.G4meM0ment.ReNature.OtherPlugins.ReTowny;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PListener implements Listener{

	private ReNature subplugin;
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private ReTowny reTowny;
	
	public PListener(RPGEssentials plugin) {
		this.plugin = plugin;
		subplugin = new ReNature();
		reTowny = new ReTowny(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.hasBlock()) return;
		if(!subplugin.getConfig().getStringList("worlds").contains((event.getClickedBlock().getWorld().getName())))
			return;
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if(reTowny.isTown(event.getClickedBlock().getLocation()))
			return;
		
		Material itemType = event.getPlayer().getItemInHand().getType();
		if(itemType == Material.LAVA_BUCKET || itemType == Material.LAVA || itemType == Material.WATER_BUCKET || itemType == Material.WATER || itemType == Material.CAKE_BLOCK)
		{
			event.setCancelled(true);
		}
		else if(itemType == Material.BUCKET && (event.getClickedBlock().getType() == Material.LAVA || event.getClickedBlock().getType() == Material.WATER))
		{
			event.setCancelled(true);
		}
	}	
}
