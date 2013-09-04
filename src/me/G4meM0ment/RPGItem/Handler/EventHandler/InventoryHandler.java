package me.G4meM0ment.RPGItem.Handler.EventHandler;

import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.ListHandler;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItemHandler;
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
	private CustomItemHandler customItemHandler;
	
	public InventoryHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
	}
	public InventoryHandler() {
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
	}
	
	public void processInventory(Inventory inv) {
		if(inv == null) return;
		
		for(ItemStack item : inv.getContents()) {
			if(itemHandler.isCustomItem(item)) {
				ItemMeta meta = item.getItemMeta();
				String name = ChatColor.stripColor(meta.getDisplayName());
				List<String> lore = meta.getLore();
				int size = lore.size();
				List<CustomItem> list;
				
				if(ListHandler.getCustomItemTypeList(ChatColor.stripColor(name)) == null) {
					lh.initializeList(ChatColor.stripColor(name));
					list = ListHandler.getCustomItemTypeList(ChatColor.stripColor(name));
				} else {
					list = ListHandler.getCustomItemTypeList(ChatColor.stripColor(name));
				}
				if(!lh.containsCustomItem(list, item)) {
					FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
					FileConfiguration data = itemData.getDataFile(itemData.getFile(name));
					int id = Integer.valueOf(ChatColor.stripColor(lore.get(size-1)));

					ListHandler.addCustomItemToList(new CustomItem(item, ChatColor.stripColor(meta.getDisplayName()), id, config.getInt("skinId"), config.getInt("damage"), config.getInt("damageMax"),
						data.getInt(Integer.toString(id)+".durability"), ChatColor.stripColor(lore.get(size-3)), config.getInt("price"), ChatColor.stripColor(lore.get(size-2)), 
						Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand")), list);
				}
				else {
					customItemHandler.updateItem(item);
				}
			}
		}
	}
}
