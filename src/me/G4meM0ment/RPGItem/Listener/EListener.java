package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.DamageHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EListener implements Listener{

	private RPGEssentials plugin;
	private ItemHandler itemHandler;
	private DamageHandler dmgHandler;
	private CustomItemHandler customItemHandler;
	
	public EListener(RPGEssentials plugin) {
		this.plugin = plugin;
		itemHandler = new ItemHandler();
		dmgHandler = new DamageHandler();
		customItemHandler = new CustomItemHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Player p = null, e = null;
		if(event.getDamager() instanceof Player)
			p = (Player) event.getDamager();
		if(event.getDamager() instanceof Projectile) {
			if(((Projectile)event.getDamager()).getShooter() instanceof Player)
				p = (Player) ((Projectile)event.getDamager()).getShooter();
		}
		if(event.getEntity() instanceof Player)
			e = (Player) event.getEntity();
		
		if(p != null) {
			double newDmg = dmgHandler.handleDamageEvent(p);
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