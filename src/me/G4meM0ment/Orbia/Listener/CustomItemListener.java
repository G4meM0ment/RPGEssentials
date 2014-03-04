package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGItem.Handler.ItemHandler;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener implements Listener {
	
	private ItemHandler itemHandler;
	private List<Material> remove = new ArrayList<Material>();
	
	public CustomItemListener() {
		itemHandler = new ItemHandler();
		
		remove.add(Material.WOOD_AXE);
		remove.add(Material.WOOD_HOE);
		remove.add(Material.WOOD_PICKAXE);
		remove.add(Material.WOOD_SPADE);
		remove.add(Material.WOOD_PICKAXE);
		remove.add(Material.WOOD_SWORD);
		
		remove.add(Material.STONE_AXE);
		remove.add(Material.STONE_HOE);
		remove.add(Material.STONE_PICKAXE);
		remove.add(Material.STONE_SPADE);
		remove.add(Material.STONE_SWORD);
		
		remove.add(Material.LEATHER_BOOTS);
		remove.add(Material.LEATHER_CHESTPLATE);
		remove.add(Material.LEATHER_HELMET);
		remove.add(Material.LEATHER_LEGGINGS);
		
		remove.add(Material.IRON_AXE);
		remove.add(Material.IRON_BARDING);
		remove.add(Material.IRON_BOOTS);
		remove.add(Material.IRON_CHESTPLATE);
		remove.add(Material.IRON_HELMET);
		remove.add(Material.IRON_HOE);
		remove.add(Material.IRON_LEGGINGS);
		remove.add(Material.IRON_PICKAXE);
		remove.add(Material.IRON_SPADE);
		remove.add(Material.IRON_SWORD);
		
		remove.add(Material.GOLD_AXE);
		remove.add(Material.GOLD_BARDING);
		remove.add(Material.GOLD_BOOTS);
		remove.add(Material.GOLD_CHESTPLATE);
		remove.add(Material.GOLD_HELMET);
		remove.add(Material.GOLD_HOE);
		remove.add(Material.GOLD_LEGGINGS);
		remove.add(Material.GOLD_PICKAXE);
		remove.add(Material.GOLD_SPADE);
		remove.add(Material.GOLD_SWORD);
		
		remove.add(Material.DIAMOND_AXE);
		remove.add(Material.DIAMOND_BARDING);
		remove.add(Material.DIAMOND_BOOTS);
		remove.add(Material.DIAMOND_CHESTPLATE);
		remove.add(Material.DIAMOND_HELMET);
		remove.add(Material.DIAMOND_HOE);
		remove.add(Material.DIAMOND_LEGGINGS);
		remove.add(Material.DIAMOND_PICKAXE);
		remove.add(Material.DIAMOND_SPADE);
		remove.add(Material.DIAMOND_SWORD);
		
		remove.add(Material.BOW);
		remove.add(Material.FISHING_ROD);
		remove.add(Material.SHEARS);
		remove.add(Material.FLINT_AND_STEEL);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		ItemStack i = event.getItem().getItemStack();
		if(p == null) return;
		if(i == null) return;
		if(!p.hasPermission("orbia.keepitems") && !p.getGameMode().equals(GameMode.CREATIVE)) return;
		
		if(!itemHandler.isCustomItem(i) && remove.contains(i.getType())) {
			event.getItem().remove();
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getInventory().getHolder() instanceof Player)) return;
		Player p = (Player) event.getInventory().getHolder();
		ItemStack i = event.getCurrentItem();
		if(p == null) return;
		if(i == null) return;
		if(!p.hasPermission("orbia.keepitems") && !p.getGameMode().equals(GameMode.CREATIVE)) return;

		if(!itemHandler.isCustomItem(i) && remove.contains(i.getType()))
			p.getInventory().remove(i);
	}	
}
