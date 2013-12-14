package me.G4meM0ment.RPGItem.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.herocraftonline.heroes.characters.CharacterDamageManager.ProjectileType;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class MetaHandler {
	
	private static RPGEssentials plugin;
	private RPGItem subplugin;
	private ItemConfig itemConfig;
	
	private static int splitter = 39;
	private static Material[] armor = 
	{
		Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLD_HELMET, Material.DIAMOND_HELMET,
		Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
		Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS,
		Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS, Material.DIAMOND_BOOTS,
		Material.IRON_BARDING, Material.GOLD_BARDING, Material.DIAMOND_BARDING
	};
	
	public MetaHandler(RPGEssentials plugin)
	{
		MetaHandler.plugin = plugin;
		subplugin = new RPGItem();
		itemConfig = new ItemConfig();
	}
	public MetaHandler() 
	{
		subplugin = new RPGItem();
		itemConfig = new ItemConfig();
	}
	
	public static int getSplitter() 
	{
		return splitter;
	}
	public static void setSplitter(int splitter) 
	{
		MetaHandler.splitter = splitter;
	}

	protected List<String> getLore(CustomItem customItem, Player p) 
	{
		List<String> lore = new ArrayList<String>();
		List<String> preLore = new ArrayList<String>();
		int dmg = 0;
		
		if(customItem == null) return lore;
		if(plugin.getHeroes() != null && p != null) 
		{
			Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
			HeroClass hClass = h.getHeroClass();
			double baseDamage = 0;
			if(isArmor(customItem.getItem()))
				dmg = getArmorProtection(customItem.getItem());
			else
			{
				if(customItem.getItem().getType() == Material.BOW)
					baseDamage = hClass.getProjectileDamage(ProjectileType.ARROW);
				else
					if(hClass.getItemDamage(customItem.getItem().getType()) != null)
						baseDamage = hClass.getItemDamage(customItem.getItem().getType());
			
				dmg = (int) (baseDamage+(hClass.getItemDamageLevel(customItem.getItem().getType())*h.getLevel()));
			}
		}
		
		if(customItem.getDurability() >= 0 && !subplugin.getConfig().getString("ItemTooltipGeneralDurabilityFormat").isEmpty())
			preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipGeneralDurabilityFormat").replace("%actual", Integer.toString(customItem.getDurability())).replace("%max", Integer.toString(customItem.getMaxDurability()))));
		
		if(!customItem.getHand().isEmpty() || !customItem.getType().isEmpty() && !subplugin.getConfig().getString("ItemTooltipGeneralInfoFormat").isEmpty())
			preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipGeneralInfoFormat").replace("%hand", customItem.getHand()).replace("%type", customItem.getType())));
		
		if(isArmor(customItem.getItem()))
			if(!subplugin.getConfig().getString("ItemTooltipArmorFormat").isEmpty())
				preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipArmorFormat").replace("%dmgValue", Integer.toString(customItem.getDmgValue()+dmg)).replace("%maxDmgValue", Integer.toString(customItem.getDmgValueMax()+dmg))));
		else
			if(!subplugin.getConfig().getString("ItemTooltipDamageFormat").isEmpty())
				preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipDamageFormat").replace("%dmgValue", Integer.toString(customItem.getDmgValue()+dmg)).replace("%maxDmgValue", Integer.toString(customItem.getDmgValueMax()+dmg))));
		
		if(!subplugin.getConfig().getString("ItemTooltipPriceFormat").isEmpty())
			preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipPriceFormat").replace("%priceValue", Integer.toString(customItem.getPrice()))));
		
		if(!customItem.getDesc().isEmpty() && !subplugin.getConfig().getString("ItemTooltipDescriptionFormat").isEmpty())
			preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipDescriptionFormat").replace("%description", customItem.getDesc())));
		
		if(!customItem.getLore().isEmpty() && !subplugin.getConfig().getString("ItemTooltipLoreFormat").isEmpty())
			preLore.add(ChatColor.translateAlternateColorCodes('&', subplugin.getConfig().getString("ItemTooltipLoreFormat").replace("%lore", customItem.getLore())));
		
		for(String s : preLore) 
		{
			if(s.length() < splitter)
			{
				lore.add(s);
				continue;
			}
			for(int i = 0; i <= s.length(); i += splitter) 
			{
				if(i+splitter > s.length())
				{
					lore.add(s.substring(i));
					break;
				}
				lore.add(s.substring(i, i+splitter));
			}
		}
		
		lore.add(ChatColor.BLACK+Integer.toString(customItem.getId()));
		return lore;
	}
	
	public LeatherArmorMeta getLeatherArmorMeta(CustomItem customItem, Player p) 
	{
		LeatherArmorMeta meta = (LeatherArmorMeta) customItem.getItem().getItemMeta();
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		
		meta.setColor(Color.fromRGB(config.getInt("color.r"), config.getInt("color.g"), config.getInt("color.b")));
		meta.setDisplayName(Quality.valueOf(config.getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem, p));
		return meta;
	}
	
	public ItemMeta getItemMeta(CustomItem customItem, Player p) 
	{
		ItemMeta meta = (ItemMeta) customItem.getItem().getItemMeta();
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		
		meta.setDisplayName(Quality.valueOf(config.getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem, p));
		return meta;
	}

	private boolean isArmor(ItemStack item)
	{
		if(item == null) return false;
		Material iM = item.getType();
		for(Material m : armor)
			if(m == iM)
				return true;
		return false;
	}
	private int getArmorProtection(ItemStack item)
	{
		switch(item.getType())
		{
		case LEATHER_HELMET:
			return 1;
		case LEATHER_CHESTPLATE:
			return 3;
		case LEATHER_LEGGINGS:
			return 2;
		case LEATHER_BOOTS:
			return 1;
		
		case CHAINMAIL_HELMET:
			return 2;
		case CHAINMAIL_CHESTPLATE:
			return 5;
		case CHAINMAIL_LEGGINGS:
			return 4;
		case CHAINMAIL_BOOTS:
			return 1;
			
		case IRON_HELMET:
			return 2;
		case IRON_CHESTPLATE:
			return 6;
		case IRON_LEGGINGS:
			return 5;
		case IRON_BOOTS:
			return 2;
			
		case GOLD_HELMET:
			return 2;
		case GOLD_CHESTPLATE:
			return 5;
		case GOLD_LEGGINGS:
			return 4;
		case GOLD_BOOTS:
			return 1;
			
		case DIAMOND_HELMET:
			return 3;
		case DIAMOND_CHESTPLATE:
			return 8;
		case DIAMOND_LEGGINGS:
			return 6;
		case DIAMOND_BOOTS:
			return 3;
			
		case IRON_BARDING:
			return 1;
		case GOLD_BARDING:
			return 2;
		case DIAMOND_BARDING:
			return 3;
		}
		return 0;
	}
}
