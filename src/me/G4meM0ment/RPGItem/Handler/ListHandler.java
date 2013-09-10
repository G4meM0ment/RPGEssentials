package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;

public class ListHandler {
	
	private ItemHandler itemHandler;
	
	private static HashMap<String, List<CustomItem>> customItemTypes = new HashMap<String, List<CustomItem>>();
	private static String powers[] = new String[] {"speed","scuba","jump","nightvision","invisibility"};
	private static String enchantments[] = new String[] {"arrow_damage", "arrow_fire", "arrow_infinite", "arrow_knockback", "damage_all", "damage_arthropods", "damage_undead", "dig_speed", "fire_aspect", "knockback", "loot_block", "loot_mob", "oxygen", "protection_enviroment", "protection_explosion", "protection_fall", "protection_fire", "protection_projectile", "silk_touch", "thorns", "water_worker"};
	
	public ListHandler() {
		itemHandler = new ItemHandler();
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

	public String[] getPowers() {
		return powers;
	}
	
	public String[] getEnchantments() {
		return enchantments;
	}
	
	public void updateItems() {
		for(List<CustomItem> list : getCustomItemTypes().values()) {
			for(CustomItem cItem : list) {
				ItemConfig itemConfig = new ItemConfig();
				CustomItemHandler customItemHandler = new CustomItemHandler();
				FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName()));
				cItem.setData(config.getInt("data"));
				cItem.setDmgValue(config.getInt("damage"));
				cItem.setDesc(config.getString("description"));
				cItem.setDmgValueMax(config.getInt("damageMax"));
				cItem.setHand(config.getString("hand"));
				cItem.setLore(config.getString("lore"));
				cItem.setPrice(config.getInt("price"));
				cItem.setQuality(Quality.valueOf(config.getString("quality").toUpperCase()));
				cItem.setSkinId(config.getInt("skinId"));
				cItem.setType(config.getString("type"));
				
				customItemHandler.updateItem(cItem.getItem(), null, true);
			}
		}
	}
}
