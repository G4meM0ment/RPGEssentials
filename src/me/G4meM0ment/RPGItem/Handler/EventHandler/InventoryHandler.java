package me.G4meM0ment.RPGItem.Handler.EventHandler;

import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.ListHandler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHandler {
	
	@SuppressWarnings("unused")
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
	
	public void processInventory(Inventory inv, Player p) 
	{
		if(inv == null) return;
		for(ItemStack item : inv.getContents())
		{
			if(itemHandler.isCustomItem(item)) 
			{
				ItemMeta meta = item.getItemMeta();
				String name = ChatColor.stripColor(meta.getDisplayName());
				List<String> lore = meta.getLore();
				int size = lore.size();
				List<CustomItem> list;
				
				if(ListHandler.getCustomItemTypeList(name) == null) 
				{
					lh.initializeList(name);
					list = ListHandler.getCustomItemTypeList(name);
				} else 
				{
					list = ListHandler.getCustomItemTypeList(name);
				}
				if(!lh.containsCustomItem(list, item)) 
				{
					FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
					FileConfiguration data = itemData.getDataFile(itemData.getFile(name));
					int id = Integer.valueOf(ChatColor.stripColor(lore.get(size-1)));
					
					if(config == null) return;

					ListHandler.addCustomItemToList(new CustomItem(item, name, id, config.getInt("data"), Material.valueOf(config.getString("skin").toUpperCase()), config.getInt("damage"), config.getInt("damageMax"),
						data.getInt(Integer.toString(id)+".durability"), config.getString("description"), config.getInt("price"), config.getString("lore"), 
						Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand"), Material.valueOf(config.getString("repair").toUpperCase()), config.getInt("durability")), list);
					customItemHandler.updateItem(item, p, false);
					return;
				}
			  customItemHandler.updateItem(item, p, false);
			}
		}
	}
}
