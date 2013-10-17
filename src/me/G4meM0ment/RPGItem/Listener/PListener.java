package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.PermHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.InventoryHandler;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PListener implements Listener{
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	private ItemData itemData;
	private PermHandler permHandler;
	private ItemHandler itemHandler;
	private CustomItemHandler customItemHandler;
	private RPGItem subplugin;
	
	public PListener(RPGEssentials plugin) {
		this.plugin = plugin;
		subplugin = new RPGItem();
		invHandler = new InventoryHandler();
		itemData = new ItemData();
		permHandler = new PermHandler(plugin);
		itemHandler = new ItemHandler();
		customItemHandler = new CustomItemHandler();
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
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(!(event.getClickedBlock() instanceof Block)) return;
		if(p == null || !itemHandler.isCustomItem(p.getItemInHand())) return;
		
		ItemMeta meta = p.getItemInHand().getItemMeta();
		CustomItem cItem = customItemHandler.getCustomItem(ChatColor.stripColor(meta.getDisplayName()), Integer.parseInt(ChatColor.stripColor(meta.getLore().get(meta.getLore().size()-1))));
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
				Block b = event.getClickedBlock();
				if(b.getType() == Material.ANVIL && p.getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
				if(permHandler.hasRPGItemRepairPerms(p) && hasItemInInv(p, customItemHandler.getRepairMaterial(cItem), 1)) {
					customItemHandler.repairCustomItem(cItem, subplugin.getConfig().getInt("RepairAmountPerRepair"));
					p.playSound(b.getLocation(), Sound.ANVIL_USE, 50, 1);
					p.getInventory().removeItem(new ItemStack(customItemHandler.getRepairMaterial(cItem), 1));
					event.setCancelled(true);
				}
		} else if(event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(cItem.getDurability() == 0)
				event.setCancelled(true);
		}
	}
	
	public boolean hasItemInInv(Player p, Material m, int amount) {
		int counted = 0;
		for(ItemStack i : p.getInventory()) {
			if(i == null) continue;
			if(i.getType() == m) {
				if(i.getAmount() >= amount)
					return true;
				else
					counted += i.getAmount();
			}
		}
		if(counted >= amount)
			return true;
		
		return false;
	}
	
}
