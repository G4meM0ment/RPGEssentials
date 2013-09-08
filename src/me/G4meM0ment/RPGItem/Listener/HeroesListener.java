package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.DamageHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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
		if(event.isProjectile()) {
			if(((Projectile)event.getDamager()).getShooter() instanceof Player)
				p = (Player) ((Projectile)event.getDamager()).getShooter();
		}
		if(event.getEntity() instanceof Player)
			e = (Player) event.getEntity();
		
		if(p != null) {
			double newDmg = event.getDamage()+dmgHandler.handleDamageEvent(p);
			if(itemHandler.isCustomItem(p.getItemInHand()))
				customItemHandler.itemUsed(customItemHandler.getCustomItem(ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName()),
						Integer.valueOf(ChatColor.stripColor(p.getItemInHand().getItemMeta().getLore().get(p.getItemInHand().getItemMeta().getLore().size()-1)))));
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
