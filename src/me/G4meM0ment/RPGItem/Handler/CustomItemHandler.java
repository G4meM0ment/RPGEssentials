package me.G4meM0ment.RPGItem.Handler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;

public class CustomItemHandler {
	
	private RPGEssentials plugin;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	private EnchantmentHandler enchantHandler;
	private MetaHandler metaHandler;
	
	public CustomItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		enchantHandler = new EnchantmentHandler();
		metaHandler = new MetaHandler(plugin);
	}
	public CustomItemHandler() {
		lh = new ListHandler();	
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		enchantHandler = new EnchantmentHandler();
		metaHandler = new MetaHandler();
	}
	
	public void spawnCustomItem(Player p, CustomItem customItem) {
		//Variable decleration
		ItemStack item = new ItemStack(customItem.getSkinId(), 1);
		customItem.setItem(item);
		FileConfiguration data = itemData.getDataFile(itemData.getFile(customItem.getDispName()));
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		List<CustomItem> list = ListHandler.getCustomItemTypeList(customItem.getDispName());
		
		//Configuring item
		if(isColorable(item))
			item.setItemMeta(metaHandler.getLeatherArmorMeta(customItem, p));
		else
			item.setItemMeta(metaHandler.getItemMeta(customItem, p));
		item.setData(new MaterialData(customItem.getData()));
		enchantHandler.addEnchantments(item, config);
		
		//setting up id specific durability
		data.set(Integer.toString(customItem.getId())+".durability", config.getInt("durability"));
        try {
        	data.save(itemData.getFile(customItem.getDispName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

        //adding item to list
        if(list == null)
        	lh.initializeList(customItem.getDispName());
		ListHandler.addCustomItemToList(customItem, list);
		
		p.getInventory().addItem(item);
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

	public void updateItem(ItemStack item, Player p, boolean fullUpdate) {
		int id = 0;
		try {
			id = Integer.valueOf((ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
		}catch (NumberFormatException e) {}
		if(id == 0) return;
		
		CustomItem customItem = getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()), id);
		if(customItem == null) return;
		ItemMeta meta = item.getItemMeta();
		File config = itemConfig.getFile(customItem.getDispName());
		File data = itemData.getFile(customItem.getDispName());
		
		customItem.setData(itemConfig.getConfig(config).getInt("data"));
		customItem.setSkinId(itemConfig.getConfig(config).getInt("skinId"));
		customItem.setDmgValue(itemConfig.getConfig(config).getInt("damage"));
		customItem.setDmgValueMax(itemConfig.getConfig(config).getInt("damageMax"));
		customItem.setDurability(itemData.getDataFile(data).getInt(customItem.getId()+".durability"));
		customItem.setDesc(itemConfig.getConfig(config).getString("description"));
		customItem.setPrice(itemConfig.getConfig(config).getInt("price"));
		customItem.setLore(itemConfig.getConfig(config).getString("lore"));
		customItem.setQuality(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase()));
		customItem.setType(itemConfig.getConfig(config).getString("type"));
		customItem.setHand(itemConfig.getConfig(config).getString("hand"));
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(metaHandler.getLore(customItem, p));
//		itemData.getDataFile(data).set(Integer.toString(customItem.getId())+".durability", itemConfig.getConfig(config).getInt("durability"));
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		item.setTypeId(customItem.getSkinId());	
		if(fullUpdate) {
			enchantHandler.removeEnchantments(item);
			enchantHandler.addEnchantments(item, itemConfig.getConfig(config));
		}
		item.setData(new MaterialData(customItem.getData()));
	}
	
	public void registerItem(ItemStack item, int id) {
		if(item == null) return;
		if(itemConfig.getFile(ChatColor.stripColor(item.getItemMeta().getDisplayName())) == null) return;
		if(id <= 0) id = getFreeId(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
		ItemMeta meta = item.getItemMeta();
		CustomItem customItem = new CustomItem(item, ChatColor.stripColor(meta.getDisplayName()), id, 0, item.getType().getId(),
				0 , 0, 0, "", 0, "", Quality.TRASH, "", "");
		
		FileConfiguration data = itemData.getDataFile(itemData.getFile(customItem.getDispName()));
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		List<CustomItem> list = ListHandler.getCustomItemTypeList(customItem.getDispName());

		
		//Configuring item
		if(isColorable(item))
			item.setItemMeta(metaHandler.getLeatherArmorMeta(customItem, null));
		else
			item.setItemMeta(metaHandler.getItemMeta(customItem, null));
		enchantHandler.addEnchantments(item, config);
		item.getItemMeta().getLore().add(ChatColor.BLACK+Integer.toString(id));
		
		//setting up id specific durability
		data.set(Integer.toString(customItem.getId())+".durability", config.getInt("durability"));
        try {
        	data.save(itemData.getFile(customItem.getDispName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

        //adding item to list
        if(list == null)
        	lh.initializeList(customItem.getDispName());
		ListHandler.addCustomItemToList(customItem, list);
		
		updateItem(item, null, true);
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
		if(durability <= 0)
			durability = 1;
		item.setDurability((short) durability);
	}
	
	public boolean isColorable(ItemStack item) {
		if(item.getTypeId() == 298 || item.getTypeId() == 299 || item.getTypeId() == 300 || item.getTypeId() == 301)
			return true;
		else
			return false;
	}
}
