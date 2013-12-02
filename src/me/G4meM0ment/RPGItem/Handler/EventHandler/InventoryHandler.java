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
import me.G4meM0ment.RPGItem.Handler.PowerHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
	private PowerHandler powerHandler;
	
	public InventoryHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
		powerHandler = new PowerHandler();
		
		startInventoryChecker();
	}
	public InventoryHandler() {
		itemHandler = new ItemHandler();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		customItemHandler = new CustomItemHandler();
		powerHandler = new PowerHandler();
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
	
	private void startInventoryChecker() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					processArmor(p);
					processItem(p);
				}
			}
		}, 0, 60);
	}
	
	public void processArmor(Player p) {
		if(p == null) return;
		powerHandler.clearPowers(p);
		for(ItemStack item : p.getInventory().getArmorContents()) {
			if(itemHandler.isCustomItem(item)) {
				ItemMeta meta = item.getItemMeta();
				String name = ChatColor.stripColor(meta.getDisplayName());
				List<String> lore = meta.getLore();
				int size = lore.size();

				int id = Integer.valueOf(ChatColor.stripColor(lore.get(size-1)));
				CustomItem cItem = customItemHandler.getCustomItem(name, id);

				powerHandler.applyPower(p, cItem);
			}
		}
	}
	
	public void processItem(Player p) {
		if(p == null) return;
		ItemStack item = p.getItemInHand();
		if(itemHandler.isCustomItem(item)) {
			ItemMeta meta = item.getItemMeta();
			String name = ChatColor.stripColor(meta.getDisplayName());
			List<String> lore = meta.getLore();
			int size = lore.size();

			int id = Integer.valueOf(ChatColor.stripColor(lore.get(size-1)));
			CustomItem cItem = customItemHandler.getCustomItem(name, id);

			powerHandler.applyPower(p, cItem);
		}
	}
}
