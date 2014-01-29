package me.G4meM0ment.RPGItem.Handler.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.ListHandler;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private RPGItem subplugin;
	private ItemHandler itemHandler;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	private CustomItemHandler customItemHandler;
	
	public InventoryHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		subplugin = new RPGItem();
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
	}
	public InventoryHandler() {
		subplugin = new RPGItem();
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
	}
	
	public void processInventory(PlayerInventory inv, Player p) 
	{
		if(inv == null) return;		
		
		ItemStack[] allItems = Arrays.copyOf(inv.getArmorContents(), inv.getArmorContents().length + inv.getContents().length);
		  System.arraycopy(inv.getContents(), 0, allItems, inv.getArmorContents().length, inv.getContents().length);
		
		for(ItemStack item : allItems)
		{
			if(itemHandler.isCustomItem(item)) 
			{
				ItemMeta meta = item.getItemMeta();
				String name = ChatColor.stripColor(meta.getDisplayName());
				List<String> lore = meta.getLore();
				int size = 0;
				if(lore == null && subplugin.getConfig().getBoolean("useIDs"))
					return;
				else if(lore != null)
					size = lore.size();
				List<CustomItem> list;
				
				if(ListHandler.getCustomItemTypeList(name) == null) 
				{
					lh.initializeList(name);
					list = ListHandler.getCustomItemTypeList(name);
				} else 
				{
					list = ListHandler.getCustomItemTypeList(name);
				}
				
				FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
				FileConfiguration data = itemData.getDataFile(itemData.getFile(name));
				int id = 0;
				if(subplugin.getConfig().getBoolean("useIDs"))
					id = Integer.valueOf(ChatColor.stripColor(lore.get(size-1)));
				
				if(config == null) return;
				
				if(subplugin.getConfig().getBoolean("useIDs"))
				{
					if(!lh.containsCustomItem(list, item))
						ListHandler.addCustomItemToList(new CustomItem(item, name, id, config.getInt("data"), Material.valueOf(config.getString("skin").toUpperCase()), config.getInt("damage"), config.getInt("damageMax"),
								data.getInt(Integer.toString(id)+".durability"), config.getString("description"), config.getInt("price"), config.getString("lore"), 
								Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand"), Material.valueOf(config.getString("repair").toUpperCase()), config.getInt("durability")), list);
				}
				else if (list.isEmpty())
				{
					ListHandler.addCustomItemToList(new CustomItem(item, name, id, config.getInt("data"), Material.valueOf(config.getString("skin").toUpperCase()), config.getInt("damage"), config.getInt("damageMax"),
							config.getInt("durability"), config.getString("description"), config.getInt("price"), config.getString("lore"), 
							Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand"), Material.valueOf(config.getString("repair").toUpperCase()), config.getInt("durability")), list);
				}
			  customItemHandler.updateItem(item, p, false);
			}
		}
	}
}
