package me.G4meM0ment.RPGItem.Handler;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;

public class ItemHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private RPGItem subplugin;
	@SuppressWarnings("unused")
	private ItemData itemData;
	
	public ItemHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		subplugin = new RPGItem();
		itemData = new ItemData();
	}
	public ItemHandler() {
		subplugin = new RPGItem();
		itemData = new ItemData();
	}
	
	public boolean isCustomItem(ItemStack item) {
		if(item == null) return false;
		if(!item.hasItemMeta()) return false;
		if(item.getItemMeta().getDisplayName() == null) return false;
		
		if(subplugin.getConfig().getBoolean("useIDs")) 
		{
			if(item.getItemMeta().getLore() == null) return false;
		
			int id = 0;
		
			try
			{
				//Try to get the ID 
				id = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1)));
			} catch( NumberFormatException e )
			{
				//ID not found set it to 0
				id = 0;
			}
		
			if(id > 0) 
				return true; 
			return false;
		}
		else
			return true;
	}
}
