package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	private ItemData itemData;
	
	public PListener(RPGEssentials plugin) {
		this.plugin = plugin;
		invHandler = new InventoryHandler();
		itemData = new ItemData();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Inventory i = p.getInventory();
		if(p == null || i == null) return;
		
		invHandler.processInventory(i, p);
		invHandler.processArmor(p);
		invHandler.processItem(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Inventory i = p.getInventory();
		if(p == null || i == null) return;
		
		invHandler.processInventory(i, p);
		itemData.saveDataToFiles();
	}
}
