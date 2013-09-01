package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;

public class ListHandler {
	
	private ItemHandler ItemHandler;
	
	private static HashMap<String, List<CustomItem>> customItemTypes; 
	
	public ListHandler() {
		ItemHandler = new ItemHandler();
	}
	
	public static HashMap<String, List<CustomItem>> getCustomItemTypes() {
		return customItemTypes;
	}
	
	//Getting a list from the hasmap
	public static List<CustomItem> getCustomItemTypeList(String customItemName) {
		return getCustomItemTypes().get(customItemName);
	}
	
	//get Item by its id
	public CustomItem getCustomItem(String displayName, int specificItemId) {
		for(CustomItem cItem : getCustomItemTypeList(displayName)) {
			if(cItem.getId() == specificItemId)
				return cItem;
		}
		return null;
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
