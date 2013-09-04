package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;

public class ListHandler {
	
	private ItemHandler ItemHandler;
	
	private static HashMap<String, List<CustomItem>> customItemTypes = new HashMap<String, List<CustomItem>>(); 
	
	public ListHandler() {
		ItemHandler = new ItemHandler();
		
	}
	
	public static HashMap<String, List<CustomItem>> getCustomItemTypes() {
		return customItemTypes;
	}
	
	//Getting a list from the hashmap
	public static List<CustomItem> getCustomItemTypeList(String customItemName) {
		return getCustomItemTypes().get(customItemName);
	}
	
	//Handling lists with specific custom items
	public void initializeList(String customTypeName) {
		getCustomItemTypes().put(customTypeName, new ArrayList<CustomItem>());
	}
	public static void addCustomItemToList(CustomItem customItem, List<CustomItem> list) {
		getCustomItemTypeList(customItem.getDispName()).add(customItem);
	}
	
	//check if specific custom item is already included
	public boolean containsCustomItem(List<CustomItem> list, ItemStack item) {
		for(CustomItem cItem : list) {
			if(cItem.getId() == Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)))) {
				return true;
			}
		}
		return false;
	}
}
