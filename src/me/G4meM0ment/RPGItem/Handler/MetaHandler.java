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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class MetaHandler {
	
	private static RPGEssentials plugin;
	private RPGItem rpgItem;
	private ItemConfig itemConfig;
	private static int splitter = 39;
	
	public MetaHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		rpgItem = new RPGItem();
		itemConfig = new ItemConfig();
	}
	public MetaHandler() {
		rpgItem = new RPGItem();
		itemConfig = new ItemConfig();
	}
	
	public static int getSplitter() {
		return splitter;
	}

	public static void setSplitter(int splitter) {
		MetaHandler.splitter = splitter;
	}

	protected List<String> getLore(CustomItem customItem, Player p) {

		List<String> lore = new ArrayList<String>();
		List<String> preLore = new ArrayList<String>();
		int dmg = 0;
		if(plugin.getHeroes() != null && p != null) {
			Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
			HeroClass hClass = h.getHeroClass();
			double baseDamage = 0;
			if(customItem.getItem().getType() == Material.BOW) 
				baseDamage = hClass.getItemDamage(Material.ARROW);
			else
				if(hClass.getItemDamage(customItem.getItem().getType()) != null)
					baseDamage = hClass.getItemDamage(customItem.getItem().getType());
			
			dmg = (int) (baseDamage+(hClass.getItemDamageLevel(customItem.getItem().getType())*h.getLevel()));
		}
		
		preLore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipGeneralInfoFormat").replace("%hand", customItem.getHand()).replace("%type", customItem.getType())));
		preLore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipDamageFormat").replace("%dmgValue", Integer.toString(customItem.getDmgValue()+dmg)).replace("%maxDmgValue", Integer.toString(customItem.getDmgValueMax()+dmg))));
		preLore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipPriceFormat").replace("%priceValue", Integer.toString(customItem.getPrice()))));
		preLore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipDescriptionFormat").replace("%description", customItem.getDesc())));
		preLore.add(ChatColor.translateAlternateColorCodes('&', rpgItem.getConfig().getString("ItemTooltipLoreFormat").replace("%lore", customItem.getLore())));
		
		for(String s : preLore) {
			if(s.length() < splitter) {
				lore.add(s);
				continue;
			}
			for(int i = 0; i <= s.length(); i += splitter) {
				if(i+splitter > s.length()) {
					lore.add(s.substring(i));
					break;
				}
				lore.add(s.substring(i, i+splitter));
			}
		}
		
		lore.add(ChatColor.BLACK+Integer.toString(customItem.getId()));
		return lore;
	}
	
	public LeatherArmorMeta getLeatherArmorMeta(CustomItem customItem, Player p) {
		LeatherArmorMeta meta = (LeatherArmorMeta) customItem.getItem().getItemMeta();
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		
		meta.setColor(Color.fromRGB(config.getInt("color.r"), config.getInt("color.g"), config.getInt("color.b")));
		meta.setDisplayName(Quality.valueOf(config.getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem, p));
		return meta;
	}
	
	public ItemMeta getItemMeta(CustomItem customItem, Player p) {
		ItemMeta meta = (ItemMeta) customItem.getItem().getItemMeta();
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(customItem.getDispName()));
		
		meta.setDisplayName(Quality.valueOf(config.getString("quality").toUpperCase()).colour+customItem.getDispName());
		meta.setLore(getLore(customItem, p));
		return meta;
	}

}
