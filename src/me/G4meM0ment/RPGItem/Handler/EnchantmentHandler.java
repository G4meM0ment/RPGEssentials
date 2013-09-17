package me.G4meM0ment.RPGItem.Handler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentHandler {
	
	private ListHandler lh;
	
	public EnchantmentHandler() {
		lh = new ListHandler();
	}
	
	public Enchantment getEnchantment(String enchantName) {
		return Enchantment.getByName(enchantName.toUpperCase());
	}

	protected void addEnchantments(ItemStack item, FileConfiguration config) {
		for(Enchantment e : Enchantment.values()) {
			if(config.getInt("enchantments."+e.toString()) >= 1) {
				Enchantment enchant = getEnchantment(e.toString());
				if(enchant != null)
					item.addUnsafeEnchantment(enchant, config.getInt("enchantments."+e.toString()));
			}
		}
	}
	
	protected void removeEnchantments(ItemStack item) {
		for(Enchantment e : item.getEnchantments().keySet()) {
			item.removeEnchantment(e);
		}
	}
}
