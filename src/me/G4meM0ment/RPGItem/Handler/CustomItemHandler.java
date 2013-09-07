package me.G4meM0ment.RPGItem.Handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;

public class CustomItemHandler {
	
	private RPGEssentials plugin;
	private RPGItem rpgItem;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	private ItemHandler itemHandler;
	
	public CustomItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgItem = new RPGItem();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		itemHandler = new ItemHandler();
	}
	public CustomItemHandler() {
		rpgItem = new RPGItem();
		lh = new ListHandler();
		itemHandler = new ItemHandler();	
		itemConfig = new ItemConfig();
		itemData = new ItemData();
	}
	
	public void spawnCustomItem(Player p, CustomItem customItem) {
		ItemStack item = new ItemStack(customItem.getSkinId(), 1);
		item.setData(new MaterialData(customItem.getData()));
		ItemMeta meta = item.getItemMeta();
		FileConfiguration data = itemData.getDataFile(itemData.getFile(customItem.getDispName()));
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		List<CustomItem> list = ListHandler.getCustomItemTypeList(customItem.getDispName());
		
		if(item.getTypeId() == 298 || item.getTypeId() == 299 || item.getTypeId() == 300 || item.getTypeId() == 301) {
			LeatherArmorMeta lm = (LeatherArmorMeta) item.getItemMeta();
			lm.setColor(Color.fromRGB(config.getInt("color.r"), config.getInt("color.g"), config.getInt("color.b")));
		}
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(config.getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem));
		data.set(Integer.toString(customItem.getId())+".durability", config.getInt("durability"));
        try {
        	data.save(itemData.getFile(customItem.getDispName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(list == null)
        	lh.initializeList(customItem.getDispName());
		ListHandler.addCustomItemToList(customItem, list);
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		p.getInventory().addItem(item);
	}
	
	public List<String> getLore(CustomItem customItem) {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipGeneralInfoFormat").replace("%hand", customItem.getHand()).replace("%type", customItem.getType())));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipDamageFormat").replace("%dmgValue", Integer.toString(customItem.getDmgValue())).replace("%maxDmgValue", Integer.toString(customItem.getDmgValueMax()))));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipPriceFormat").replace("%priceValue", Integer.toString(customItem.getPrice()))));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipDescriptionFormat").replace("%description", customItem.getDesc())));
		lore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipLoreFormat").replace("%lore", customItem.getLore())));
		lore.add(ChatColor.BLACK+Integer.toString(customItem.getId()));
		return lore;
	}
	
	public int getFreeId(String name) {
		int counter = 1;
		String id = itemData.getDataFile(itemData.getFile(name)).getString(counter+".durability");
		while(id != null) {
			counter++;
			id = itemData.getDataFile(itemData.getFile(name)).getString(counter+".durability");
		}
		return counter;
	}

	public void updateItem(ItemStack item) {
		CustomItem customItem = getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()), Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
		ItemMeta meta = item.getItemMeta();
		File data = itemData.getFile(customItem.getDispName());
		File config = itemConfig.getFile(customItem.getDispName());
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem));
//		itemData.getDataFile(data).set(Integer.toString(customItem.getId())+".durability", itemConfig.getConfig(config).getInt("durability"));
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		item.setTypeId(customItem.getSkinId());
	}
	
	//get Item by its id
	public CustomItem getCustomItem(String displayName, int specificItemId) {
		if(ListHandler.getCustomItemTypeList(displayName) == null) return null;
		for(CustomItem cItem : ListHandler.getCustomItemTypeList(displayName)) {
			if(cItem.getId() == specificItemId)
				return cItem;
		}
		return null;
	}
	
	public void itemUsed(CustomItem item) {
		if(item.getDurability() > 0)
			item.setDurability(item.getDurability()-1);
		repairCustomItem(item);
	}
	
	public void repairCustomItem(CustomItem cItem) {
		ItemStack item;
		if(cItem != null && cItem.getItem() != null)
			item = cItem.getItem();
		else
			return;
		int cDurability = cItem.getDurability();
		int maxCDurability = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName())).getInt("durability");
		int percent = (maxCDurability-cDurability)/maxCDurability * 100;
		int durability = (item.getType().getMaxDurability()/100)*percent;
		if(durability >= item.getType().getMaxDurability()-1)
			durability = item.getType().getMaxDurability()-1;
		item.setDurability((short) durability);
	}
}
