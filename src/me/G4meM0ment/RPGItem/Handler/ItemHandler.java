package me.G4meM0ment.RPGItem.Handler;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;

public class ItemHandler {
	
	private RPGEssentials plugin;
	private RPGItem rpgItem;
	private ItemData itemData;
	
	public ItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgItem = new RPGItem();
		itemData = new ItemData();
	}
	public ItemHandler() {
		rpgItem = new RPGItem();
		itemData = new ItemData();
	}
	
	public boolean isCustomItem(ItemStack item) {
		if(!item.hasItemMeta()) return false;
		if(item.getItemMeta().getDisplayName() == null) return false;
		if(item.getItemMeta().getLore() == null) return false;
		
		int id = 0;
		id = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
		if(id > 0) 
			return true;
		
		return false;
	}
}
