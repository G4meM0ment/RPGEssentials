package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.InventoryHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	
	public PListener(RPGEssentials plugin) {
		this.plugin = plugin;
		invHandler = new InventoryHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Inventory i = p.getInventory();
		if(p == null || i == null) return;
		
		invHandler.processInventory(i);
	}

}
