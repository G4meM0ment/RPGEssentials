package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvListener implements Listener{
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	
	public InvListener(RPGEssentials plugin) {
		this.plugin = plugin;
		invHandler = new InventoryHandler();
	}
	
	/*
	 * 
	 * 
	 * TODO find a way to not use scheduler for checking items anymore
	 * only disabled due scheduler is checking everything!
	 * 
	 * 
	 * 
	 */
	
/*	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		if(p == null) return;
		Inventory i = p.getInventory();
		
		invHandler.processInventory(i);
		invHandler.processArmor(p);
		invHandler.processItem(p);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Player p = (Player) event.getDestination().getHolder();
		if(p == null) return;
		
		invHandler.processArmor(p);
		invHandler.processItem(p);
	}
	
*/	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getInventory().getHolder() instanceof Player)) return;
		Player p = (Player) event.getInventory().getHolder();
		if(p == null) return;
		
		invHandler.processInventory(p.getInventory(), p);
		//invHandler.processArmor(p);
		//invHandler.processItem(p);
	}
}
