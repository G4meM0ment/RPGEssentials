package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;

public class ItemHandler {
	
	private RPGEssentials plugin;
	private RPGItem rpgItem;
	private ItemData itemData;
	
	private String splitter;
	private static List[] itemLists;
	private static HashMap<String, List<CustomItem>> customItemTypes; 
	
	public ItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgItem = new RPGItem();
		itemData = new ItemData();
		customItemTypes = new HashMap<String, List<CustomItem>>();
	}
	public ItemHandler() {
		rpgItem = new RPGItem();
		itemData = new ItemData();
	}
	
	public boolean isCustomItem(ItemStack item) {
		if(!item.hasItemMeta()) return false;
		if(item.getItemMeta().getDisplayName() == null) return false;
		
		int id = Integer.parseInt(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1));
		if(id > 0) 
			return true;
		
		return false;
	}

}
