package me.G4meM0ment.RPGItem.Handler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;

public class CustomItemHandler {
	
	private RPGEssentials plugin;
	private RPGItem subplugin;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private ListHandler lh;
	private EnchantmentHandler enchantHandler;
	private MetaHandler metaHandler;
	private ItemHandler itemHandler;
	private PowerHandler powerH;
	
	private static Material[] repairable = {
		Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD,
		Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE,
		Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE,
		Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE,
		Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE,
		Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLD_HELMET, Material.DIAMOND_HELMET,
		Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
		Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS,
		Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS, Material.DIAMOND_BOOTS,
		Material.IRON_BARDING, Material.GOLD_BARDING, Material.DIAMOND_BARDING,
		Material.FLINT_AND_STEEL, Material.SHEARS, Material.FISHING_ROD, Material.BOW,
	};
	
	public CustomItemHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		subplugin = new RPGItem();
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		lh = new ListHandler();
		enchantHandler = new EnchantmentHandler();
		metaHandler = new MetaHandler(plugin);
		itemHandler = new ItemHandler();
		powerH = new PowerHandler(this);
	}
	public CustomItemHandler() 
	{
		subplugin = new RPGItem();
		lh = new ListHandler();	
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		enchantHandler = new EnchantmentHandler();
		metaHandler = new MetaHandler();
		itemHandler = new ItemHandler();
		powerH = new PowerHandler(this);
	}
	
	public void initializeAutoRepair()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{			
				for(Player p : Bukkit.getOnlinePlayers())
				{
					repairItems(p);
				}
			}
		}, 0 , 200);
	}
	
	public CustomItem spawnCustomItem(Player p, CustomItem customItem) 
	{
		//Variable decleration
		ItemStack item = new ItemStack(customItem.getSkin(), 1);
		customItem.setItem(item);
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		List<CustomItem> list = ListHandler.getCustomItemTypeList(customItem.getDispName());
		
		//Configuring item
		if(isColorable(item))
			item.setItemMeta(metaHandler.getLeatherArmorMeta(customItem, p));
		else
			item.setItemMeta(metaHandler.getItemMeta(customItem, p));
		item.setData(new MaterialData(customItem.getData()));
		enchantHandler.addEnchantments(item, config);
		
        if(subplugin.getConfig().getBoolean("useIDs"))
        {
    		FileConfiguration data = itemData.getDataFile(itemData.getFile(customItem.getDispName()));
        	//setting up id specific durability
        	data.set(Integer.toString(customItem.getId())+".durability", config.getInt("durability"));
        	try 
        	{
        		data.save(itemData.getFile(customItem.getDispName()));
        	} catch (IOException e) {}
        }
		
        //adding item to list
        if(list == null)
        	lh.initializeList(customItem.getDispName());
        if(subplugin.getConfig().getBoolean("useIDs"))
        	ListHandler.addCustomItemToList(customItem, list);
        else if(ListHandler.getCustomItemTypeList(customItem.getDispName()).isEmpty())
        	ListHandler.addCustomItemToList(customItem, list);
        
        if(p.getInventory().firstEmpty() == -1) {
        	p.getWorld().dropItem(p.getLocation(), item);
        	Messenger.sendMessage(p, ChatColor.GRAY+"Du hast "+ChatColor.WHITE+customItem.getDispName()+ChatColor.GRAY+" erhalten");
        	Messenger.sendMessage(p, ChatColor.GRAY+"Dein Inventar ist überfüllt, der Gegestand wurde auf den Boden geworfen");
        } else {
        	p.getInventory().addItem(item);
        	Messenger.sendMessage(p, ChatColor.GRAY+"Du hast "+ChatColor.WHITE+customItem.getDispName()+ChatColor.GRAY+" erhalten");
        }
		return customItem;
	}
	
	public int getFreeId(String name) 
	{
		int counter = 1;
		String id = itemData.getDataFile(itemData.getFile(name)).getString(counter+".durability");
		while(id != null) 
		{
			counter++;
			id = itemData.getDataFile(itemData.getFile(name)).getString(counter+".durability");
		}
		return counter;
	}

	public void updateItem(ItemStack item, Player p, boolean full) 
	{
		int id = 0;		
		if(subplugin.getConfig().getBoolean("useIDs"))
		{

			try {
				id = Integer.valueOf((ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
			}catch (NumberFormatException e) {}
			if(id == 0) return;
		}
		
		CustomItem customItem = getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()), id);
		if(customItem == null) return;
		ItemMeta meta = item.getItemMeta();
		File config = itemConfig.getFile(customItem.getDispName());
		File data = itemData.getFile(customItem.getDispName());
		
		customItem.setData(itemConfig.getConfig(config).getInt("data"));
		customItem.setSkin(Material.valueOf(itemConfig.getConfig(config).getString("skin").toUpperCase()));
		customItem.setDmgValue(itemConfig.getConfig(config).getInt("damage"));
		customItem.setDmgValueMax(itemConfig.getConfig(config).getInt("damageMax"));
		if(full && subplugin.getConfig().getBoolean("useIDs"))
			customItem.setDurability(itemData.getDataFile(data).getInt(customItem.getId()+".durability"));
		customItem.setDesc(itemConfig.getConfig(config).getString("description"));
		customItem.setPrice(itemConfig.getConfig(config).getInt("price"));
		customItem.setLore(itemConfig.getConfig(config).getString("lore"));
		customItem.setQuality(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase()));
		customItem.setType(itemConfig.getConfig(config).getString("type"));
		customItem.setHand(itemConfig.getConfig(config).getString("hand"));
		customItem.setMaxDurability(itemConfig.getConfig(config).getInt("durability"));
		
		HashMap<String, Double> powers = new HashMap<String, Double>();
		for(String s : powerH.getItemPowers(customItem))
			powers.put(s, itemConfig.getConfig(itemConfig.getFile(customItem.getDispName())).getDouble("powers."+s));
		customItem.setPowers(powers);
		
		if(customItem.getMaxDurability() < 0)
		{
			customItem.setDurability(-1);
			if(subplugin.getConfig().getBoolean("useIDs"))
			{
				itemData.getDataFile(data).set(Integer.toString(customItem.getId())+".durability", -1);
				try 
				{
					itemData.getDataFile(data).save(data);
				} catch (IOException e) {}
			}
		}
		
		//set the meta information & get id specific values
		meta.setDisplayName(Quality.valueOf(itemConfig.getConfig(config).getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(metaHandler.getLore(customItem, p));
		customItem.setItem(item);
		
		item.setItemMeta(meta);
		item.setType(customItem.getSkin());	
		item.setData(new MaterialData(customItem.getData()));
		
		enchantHandler.removeEnchantments(item);
		enchantHandler.addEnchantments(item, itemConfig.getConfig(config));
	}
	
	public void registerItem(ItemStack item, int id) 
	{
		if(item == null) return;
		if(itemConfig.getFile(ChatColor.stripColor(item.getItemMeta().getDisplayName())) == null) return;
		if(id <= 0 && subplugin.getConfig().getBoolean("useIDs")) 
			id = getFreeId(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
		ItemMeta meta = item.getItemMeta();
		CustomItem customItem = new CustomItem(item, ChatColor.stripColor(meta.getDisplayName()), id, 0, item.getType(),
				0 , 0, 0, "", 0, "", Quality.TRASH, "", "", Material.AIR, 0, new HashMap<String, Double>(), false);
		
		HashMap<String, Double> powers = new HashMap<String, Double>();
		for(String s : powerH.getItemPowers(customItem))
			powers.put(s, itemConfig.getConfig(itemConfig.getFile(customItem.getDispName())).getDouble("powers."+s));
		customItem.setPowers(powers);
		
		FileConfiguration data = itemData.getDataFile(itemData.getFile(customItem.getDispName()));
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		List<CustomItem> list = ListHandler.getCustomItemTypeList(customItem.getDispName());

		//Configuring item
		if(isColorable(item))
			item.setItemMeta(metaHandler.getLeatherArmorMeta(customItem, null));
		else
			item.setItemMeta(metaHandler.getItemMeta(customItem, null));
		enchantHandler.addEnchantments(item, config);
		
		if(subplugin.getConfig().getBoolean("useIDs"))
		{
			item.getItemMeta().getLore().add(ChatColor.BLACK+Integer.toString(id));
		
			//setting up id specific durability
			data.set(Integer.toString(customItem.getId())+".durability", config.getInt("durability"));
			try 
			{
				data.save(itemData.getFile(customItem.getDispName()));
			} catch (IOException e) 
			{
				subplugin.getLogger().warning(subplugin.getLogTit()+"Could not save data file from "+customItem.getDispName());
			}
		}

        //adding item to list
        if(list == null)
        	lh.initializeList(customItem.getDispName());
        if(subplugin.getConfig().getBoolean("useIDs"))
        	ListHandler.addCustomItemToList(customItem, list);
        else if(ListHandler.getCustomItemTypeList(customItem.getDispName()).isEmpty())
        	ListHandler.addCustomItemToList(customItem, list);
		
		updateItem(item, null, true);
	}
	
	//get Item by its id
	public CustomItem getCustomItem(String displayName, int specificItemId) 
	{
		if(ListHandler.getCustomItemTypeList(displayName) == null) return null;
		if(subplugin.getConfig().getBoolean("useIDs"))
		{
			for(CustomItem cItem : ListHandler.getCustomItemTypeList(displayName)) 
			{
				if(cItem.getId() == specificItemId)
					return cItem;
			}
		}
		else
		{
			return ListHandler.getCustomItemTypeList(displayName).get(0);
		}
		return null;
	}
	public CustomItem getCustomItem(ItemStack i)
	{
		List<String> lore = i.getItemMeta().getLore();
		String name = ChatColor.stripColor(i.getItemMeta().getDisplayName());
		
		if(ListHandler.getCustomItemTypes().containsKey(name)) return null;
		
		if(subplugin.getConfig().getBoolean("useIDs"))
		{
			int id = Integer.parseInt(ChatColor.stripColor(lore.get(lore.size()-1)));
			for(CustomItem cItem : ListHandler.getCustomItemTypeList(name)) 
			{
				if(cItem.getId() == id)
					return cItem;
			}
		}
		else
		{
			if(ListHandler.getCustomItemTypeList(name).isEmpty())
			{
				registerItem(i, 0);
				//System.out.println("Debug: registered "+name+" "+ListHandler.getCustomItemTypeList(name).toString());
			}
			if(!ListHandler.getCustomItemTypeList(name).isEmpty())
				return ListHandler.getCustomItemTypeList(name).get(0);
		}
		return null;
	}
	
	public void itemUsed(ItemStack item) 
	{
		if(itemHandler.isCustomItem(item))
		{
			CustomItem cItem = getCustomItem(item);
			if(cItem.getDurability() > 0 && cItem.getMaxDurability() > 0)
				cItem.setDurability(item.getDurability()-1);
		} 				
		repairItem(item);
	}
	
	public void repairItems(Player p)
	{
		for(ItemStack i : p.getInventory().getContents())
		{
			if(i == null) continue;
			if(!subplugin.getConfig().getBoolean("DisableVanillaItemDurability"))
			{
				if(!i.hasItemMeta()) continue;
				if(itemHandler.isCustomItem(i))
				{
					if(i.getType() == Material.BOW && i.getType().getMaxDurability()-i.getDurability() > 2)
						continue;
					repairItem(i);
				}
			} 
			else
			{
				if(i.getType() == Material.BOW && i.getType().getMaxDurability()-i.getDurability() > 2)
					continue;
				repairItem(i);
			}
		}
		for(ItemStack i : p.getInventory().getArmorContents())
		{
			if(i == null) continue;
			if(!subplugin.getConfig().getBoolean("DisableVanillaItemDurability"))
			{
				if(!i.hasItemMeta()) continue;
				if(itemHandler.isCustomItem(i))
					repairItem(i);
			} 
			else
				repairItem(i);
		}
	}
	public void repairItem(ItemStack item)
	{
		if(item == null || !isRepairable(item)) return;
		
		/*double cDurability = cItem.getDurability();
		double maxCDurability = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName())).getInt("durability");
		double percent = cDurability/maxCDurability * 100;
		double durability = item.getType().getMaxDurability() - ((((double) (item.getType().getMaxDurability()))/100)*percent);
		if(durability >= item.getType().getMaxDurability()-1)
			durability = item.getType().getMaxDurability()-1;
		if(durability <= 0)
			durability = 1; */

		item.setDurability((short) 0);
	}	
    
	public void repairCustomItem(CustomItem cItem, int amount)
	{
		if(cItem == null) return;
		if(cItem.getDurability() < 0) return;
		int maxDurability = itemConfig.getConfig(itemConfig.getFile(cItem.getDispName())).getInt("durability");
		if(cItem.getDurability()+amount > maxDurability)
			cItem.setDurability(maxDurability);
		else
			cItem.setDurability(cItem.getDurability()+amount);
	}
	
	public boolean isColorable(ItemStack item) 
	{
		if(item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS)
			return true;
		else
			return false;
	}
	
	private boolean isRepairable(ItemStack item)
	{
		if(item == null) return false;
		Material iM = item.getType();
		for(Material m : repairable)
			if(m == iM)
				return true;
		return false;
	}
}
