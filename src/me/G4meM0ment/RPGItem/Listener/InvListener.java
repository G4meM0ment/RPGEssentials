package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class InvListener implements Listener{
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	private CustomItemHandler customItemHandler;
	
	public InvListener(RPGEssentials plugin) {
		this.plugin = plugin;
		invHandler = new InventoryHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		if(p == null) return;
		Inventory i = p.getInventory();
		
		invHandler.processInventory(i);
		invHandler.processItem(p);
		invHandler.processArmor(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player p = (Player) event.getPlayer();
		if(p == null) return;
		Inventory i = p.getInventory();
		
		System.out.println("Debug: Inventory opened!");
		invHandler.processInventory(i);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Player p = (Player) event.getDestination().getHolder();
		if(p == null) return;
		
		System.out.println("Debug: Item moved!");
		invHandler.processArmor(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventory(InventoryEvent event) {
		Player p = (Player) event.getInventory().getHolder();
		if(p == null) return;
		
		System.out.println("Debug: Inventory event!");
		invHandler.processItem(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getInventory().getHolder();
		if(p == null) return;
		
		System.out.println("Debug: Inventory event!");
		invHandler.processItem(p);
	}
}
