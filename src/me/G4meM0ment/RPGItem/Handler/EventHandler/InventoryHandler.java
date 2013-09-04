package me.G4meM0ment.RPGItem.Handler.EventHandler;

import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.ListHandler;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItem.Quality;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHandler {
	
	private RPGEssentials plugin;
	private ItemHandler itemHandler;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	
	public InventoryHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
	}
	public InventoryHandler() {
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
	}
	
	public void processInventory(Inventory inv) {
		if(inv == null) return;
		
		for(ItemStack item : inv.getContents()) {
			if(itemHandler.isCustomItem(item)) {
				ItemMeta meta = item.getItemMeta();
				String name = meta.getDisplayName();
				List<String> lore = meta.getLore();
				int size = lore.size();
				List<CustomItem> list = ListHandler.getCustomItemTypeList(ChatColor.stripColor(name));
				
				if(list == null) {
					lh.initializeList(ChatColor.stripColor(name));
					list = ListHandler.getCustomItemTypeList(ChatColor.stripColor(name));
				}
				if(!lh.containsCustomItem(list, item)) {
					FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
					FileConfiguration data = itemData.getDataFile(itemData.getFile(name));
					int id = Integer.valueOf(lore.get(size-1));
					
					ListHandler.addCustomItemToList(new CustomItem(item, ChatColor.stripColor(meta.getDisplayName()), id, config.getInt("skinId"), config.getInt("damage"), config.getInt("damageMax"),
						data.getInt(Integer.toString(id)+".durability"), lore.get(size-4), Integer.valueOf(ChatColor.stripColor(lore.get(size-3))), lore.get(size-2), 
						Quality.valueOf(config.getString("quality")), config.getString("type"), config.getString("hand")), list);
				}
			}
		}
	}
}
