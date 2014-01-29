package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;
import me.G4meM0ment.RPGItem.Handler.PowerHandler;
import me.G4meM0ment.RPGItem.Handler.EventHandler.DamageHandler;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.herocraftonline.heroes.api.events.WeaponDamageEvent;

public class HeroesListener implements Listener{
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private CustomItemHandler customItemHandler;
	private ItemHandler itemHandler;
	private DamageHandler dmgHandler;
	@SuppressWarnings("unused")
	private PowerHandler ph;
	
	public HeroesListener(RPGEssentials plugin) {
		this.plugin = plugin;
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
		dmgHandler = new DamageHandler();
		ph = new PowerHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onWeaponDamage(WeaponDamageEvent event) 
	{
		Player p = null, e = null;
		if(event.getAttackerEntity() instanceof Player)
			p = (Player) event.getAttackerEntity();
		
		if(event.isProjectile())
			if(event.getAttackerEntity() instanceof Projectile)
				if(((Projectile) event.getAttackerEntity()).getShooter() instanceof Player)
					p = (Player) ((Projectile)event.getAttackerEntity()).getShooter();

		if(event.getEntity() instanceof Player)
			e = (Player) event.getEntity();
			
		if(p != null) 
		{	
			ItemStack item = p.getItemInHand();
			customItemHandler.repairItems(p);
			if(itemHandler.isCustomItem(p.getItemInHand()))
			{
				CustomItem cItem = customItemHandler.getCustomItem(item);
				if(cItem != null) 
				{			
					if(cItem.getDurability() == 0)
					{
						event.setCancelled(true);
						return;
					}
					double newDmg = event.getDamage()+dmgHandler.handleDamageEvent(p);
					if(itemHandler.isCustomItem(p.getItemInHand()))
						customItemHandler.itemUsed(cItem.getItem());
					if(newDmg >= 0)
						event.setDamage(newDmg);
				}
			}
		}
		if(e != null)
		{
			double newDmg = dmgHandler.handleDamagedEvent(e, event.getDamage(), null);
			if(newDmg >= 0)
				event.setDamage(newDmg);
			customItemHandler.repairItems(e);
		}
		
		/*
		 * TODO replace with materials to hit
		 * 
		 */
/*		if(ph.hasPower(p, "poison") && p.getItemInHand().getType() == Material.SHEARS)
		{
			if(ph.getPlayersPowers(p).get("poison") == null) return;
			final Player fP = p;
			LivingEntity le = (LivingEntity) event.getEntity();
			final Double dmg = 1.0;	
			
//			EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(p, le, DamageCause.ENTITY_ATTACK, ph.getPlayersPowers(p).get("poison"));
//			Bukkit.getPluginManager().callEvent(damageEvent);
			
//			if(!damageEvent.isCancelled())
//			{
				final LivingEntity fE = le;
				final int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
				{
					@Override
					public void run() 
					{
						fE.damage(dmg, fP);
					}
				}, 0, 20);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					@Override
					public void run() 
					{
						Bukkit.getScheduler().cancelTask(taskID);
					}
				}, 80 (long) (ph.getPlayerPowers().get(fP).get("poison")*4*20));					
//			}
		} */
	}
}
