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
		switch(enchantName) {
			case "arrow_damage":
				return Enchantment.ARROW_DAMAGE;
			case "arrow_fire":
				return Enchantment.ARROW_FIRE;	
			case "arrow_infinite":
				return Enchantment.ARROW_INFINITE;
			case "arrow_knockback":
				return Enchantment.ARROW_KNOCKBACK;
			case "damage_all":
				return Enchantment.DAMAGE_ALL;
			case "damage_arthropods":
				return Enchantment.DAMAGE_ARTHROPODS;
			case "damage_undead":
				return Enchantment.DAMAGE_UNDEAD;
			case "dig_speed":
				return Enchantment.DIG_SPEED;
			case "fire_aspect":
				return Enchantment.FIRE_ASPECT;
			case "knockback":
				return Enchantment.KNOCKBACK;
			case "loot_block":
				return Enchantment.LOOT_BONUS_BLOCKS;
			case "loot_mob":
				return Enchantment.LOOT_BONUS_MOBS;
			case "oxygen":
				return Enchantment.OXYGEN;
			case "protection_enviroment":
				return Enchantment.PROTECTION_ENVIRONMENTAL;
			case "protection_explosion":
				return Enchantment.PROTECTION_EXPLOSIONS;
			case "protection_fall":
				return Enchantment.PROTECTION_FALL;
			case "protection_fire":
				return Enchantment.PROTECTION_FIRE;
			case "protection_projectile":
				return Enchantment.PROTECTION_PROJECTILE;
			case "silk_touch":
				return Enchantment.SILK_TOUCH;
			case "thorns":
				return Enchantment.THORNS;
			case "water_worker":
				return Enchantment.WATER_WORKER;
		}
		return null;
	}

	public ItemStack addEnchantments(ItemStack item, FileConfiguration config) {
		for(String s : lh.getEnchantments()) {
			if(config.getInt("enchantments."+s) >= 1) {
				Enchantment enchant = getEnchantment(s);
				if(enchant != null)
					item.addUnsafeEnchantment(enchant, config.getInt("enchantments."+s));
			}
		}
		return item;
	}
}
