package me.G4meM0ment.RPGItem.Handler.EventHandler;

import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DamageHandler {

	private CustomItemHandler customItemHandler;
	private ItemHandler itemHandler;
	
	public DamageHandler() {
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
	}
	
	public double handleDamageEvent(Player p) {
		if(p == null) return -1;
		if(p.getItemInHand() == null) return -1;
		if(!itemHandler.isCustomItem(p.getItemInHand())) return -1; 
		ItemStack item = p.getItemInHand();
		ItemMeta meta = item.getItemMeta();
		CustomItem cItem = customItemHandler.getCustomItem(ChatColor.stripColor(meta.getDisplayName()), Integer.parseInt(ChatColor.stripColor(meta.getLore().get(meta.getLore().size()-1))));

		if(cItem != null) {
			int dmg = cItem.getDmgValue();
			int maxDmg = cItem.getDmgValueMax();
			double randomDmg = ((Math.random()*(maxDmg-dmg))+dmg);
			return randomDmg;
		} else
			return -1;
	}

	public double handleDamagedEvent(Player p, double dmg, EntityDamageByEntityEvent event) {
		if(p == null) return -1;
		if(p.getInventory().getArmorContents() == null) return -1;
		ItemStack[] armor = p.getInventory().getArmorContents();
		double dmgMod = 0;
		
		for(ItemStack item : armor) {
			if(item.getType() == Material.AIR || !itemHandler.isCustomItem(item)) continue;
			if(event != null)
				dmgMod -= processArmor(event);
			else {
				CustomItem cItem = customItemHandler.getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()),
						Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
				if(cItem != null) {
					double protect = cItem.getDmgValue();
					double maxProtect = cItem.getDmgValueMax();
					double randomProtect = ((Math.random()*(maxProtect-protect))+protect)*4;
					dmgMod = dmgMod-((dmgMod*randomProtect)/100);
				} else
					continue;
			}
		}
		return dmg - ((dmg*dmgMod)/100);
	}
	
	public double processArmor(EntityDamageByEntityEvent event) {
		//TODO complex armor algorithm using entchantment, damage cause, durabilty, etc.
		return event.getDamage();
	}
	
}
