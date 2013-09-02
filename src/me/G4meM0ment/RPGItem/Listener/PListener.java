package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	private CustomItemHandler customItemHandler;
	
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
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player p = (Player) event.getPlayer();
		Inventory i = p.getInventory();
		if(p == null || i == null) return;
		
		invHandler.processInventory(i);
	}

}
