package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.DamageHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.herocraftonline.heroes.api.events.WeaponDamageEvent;

public class HeroesListener implements Listener{
	
	private CustomItemHandler customItemHandler;
	private ItemHandler itemHandler;
	private DamageHandler dmgHandler;
	
	public HeroesListener() {
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
		dmgHandler = new DamageHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onWeaponDamage(WeaponDamageEvent event) {
		Player p = null, e = null;
		if(event.getAttackerEntity() instanceof Player)
			p = (Player) event.getAttackerEntity();
		
		if(event.isProjectile())
			if(event.getAttackerEntity() instanceof Projectile)
				if(((Projectile) event.getAttackerEntity()).getShooter() instanceof Player)
					p = (Player) ((Projectile)event.getDamager()).getShooter();

		if(event.getEntity() instanceof Player)
			e = (Player) event.getEntity();
			
		if(p != null) 
		{	
			ItemStack item = p.getItemInHand();
			if(!item.hasItemMeta() || !itemHandler.isCustomItem(p.getItemInHand())) return;
			CustomItem cItem = customItemHandler.getCustomItem(ChatColor.stripColor(item.getItemMeta().getDisplayName()),
				Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1))));
			
			if(cItem.getDurability() <= 0 || cItem.getItem().getDurability() >= cItem.getItem().getType().getMaxDurability()-1)
			{
				event.setCancelled(true);
				return;
			}
			double newDmg = event.getDamage()+dmgHandler.handleDamageEvent(p);
			if(itemHandler.isCustomItem(p.getItemInHand()))
				customItemHandler.itemUsed(cItem);
			if(newDmg >= 0)
				event.setDamage(newDmg);
		}
		if(e != null) {
			double newDmg = dmgHandler.handleDamagedEvent(e, event.getDamage(), null);
			if(newDmg >= 0)
				event.setDamage(newDmg);
		}
	}

}
