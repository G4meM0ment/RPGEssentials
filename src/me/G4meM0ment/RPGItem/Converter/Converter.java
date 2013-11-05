package me.G4meM0ment.RPGItem.Converter;

import java.util.Iterator;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.MetaHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Converter {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private ItemHandler itemHandler;
	private CustomItemHandler customItemHandler;
	private MetaHandler metaHandler;
	
	public Converter(RPGEssentials plugin) {
		this.plugin = plugin;
		itemHandler = new ItemHandler();
		customItemHandler = new CustomItemHandler();
		metaHandler = new MetaHandler(plugin);
	}
	
	public void convertCustomItems(String oldName, CustomItem cloned) {
		for(World world : Bukkit.getServer().getWorlds()) {
			List<Entity> list = world.getEntities();
			Iterator<Entity> entities = list.iterator();
			while(entities.hasNext()) {
				Entity entity = entities.next();
				if(entity instanceof Item) {
					ItemStack item = (ItemStack) entity;
					if(itemHandler.isCustomItem(item) && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(oldName)) {
						CustomItem cItem = customItemHandler.getCustomItem(oldName, Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
						
						cItem.setDispName(cloned.getDispName());
						cItem.setId(customItemHandler.getFreeId(cloned.getDispName()));
						
						cItem.setData(cloned.getData());
						cItem.setDmgValue(cloned.getDmgValue());
						cItem.setDesc(cloned.getDesc());
						cItem.setDmgValueMax(cloned.getDmgValueMax());
						cItem.setHand(cloned.getHand());
						cItem.setLore(cloned.getLore());
						cItem.setPrice(cloned.getPrice());
						cItem.setQuality(cloned.getQuality());
						cItem.setSkinId(cloned.getSkinId());
						cItem.setType(cloned.getType());
						
						item.setItemMeta(metaHandler.getItemMeta(cItem, null));						
						customItemHandler.updateItem(item, null, false);
					}
				}
			}
		}
	}
}
