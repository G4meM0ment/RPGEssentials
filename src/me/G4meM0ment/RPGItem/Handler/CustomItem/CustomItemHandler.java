package me.G4meM0ment.RPGItem.Handler.CustomItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.ListHandler;

public class CustomItemHandler {
	
	private RPGEssentials plugin;
	private RPGItem rpgItem;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	
	public CustomItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgItem = new RPGItem();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
	}
	public CustomItemHandler() {
		lh = new ListHandler();
		
	}
	
	public void spawnCustomItem(Player p, CustomItem customItem) {
		ItemStack item = new ItemStack(customItem.getSkinId(), 1);
		ItemMeta meta = item.getItemMeta();
		File data = itemData.getFile(customItem.getDispName());
		File config = itemConfig.getFile(customItem.getDispName());
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase())+customItem.getDispName());
		meta.setLore(getLore(customItem));
		itemData.getConfigurationSection(data, Integer.toString(customItem.getId()), true).set("durability", itemConfig.getConfig(config).getInt("durability"));
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		p.getInventory().addItem(item);
	}
	
	public List<String> getLore(CustomItem customItem) {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipGeneralInfoFormat").replace("%hand", customItem.getHand()).replace("%type", customItem.getType())));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipDamageFormat").replace("%dmgValue", Integer.toString(customItem.getDmgValue()))));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipPriceFormat").replace("%priceValue", customItem.getPrice())));
		lore.add(ChatColor.translateAlternateColorCodes('&', customItem.getDesc()));
		lore.add(ChatColor.ITALIC+ChatColor.translateAlternateColorCodes('&', customItem.getLore()));
		lore.add(Integer.toString(customItem.getId()));
		return lore;
	}
	
	public int getFreeId(String name) {
		int counter = 1;
		ConfigurationSection section = itemData.getConfigurationSection(itemData.getFile(name), Integer.toString(counter), false);
		while(section == null) {
			counter++;
			section = itemData.getConfigurationSection(itemData.getFile(name), Integer.toString(counter), false);
		}
		return counter;
	}

	public void updateItem(ItemStack item) {
		CustomItem customItem = lh.getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()), Integer.parseInt(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
		ItemMeta meta = item.getItemMeta();
		File data = itemData.getFile(customItem.getDispName());
		File config = itemConfig.getFile(customItem.getDispName());
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase())+customItem.getDispName());
		meta.setLore(getLore(customItem));
		itemData.getConfigurationSection(data, Integer.toString(customItem.getId()), true).set("durability", itemConfig.getConfig(config).getInt("durability"));
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		item.setTypeId(customItem.getSkinId());
	}
}
