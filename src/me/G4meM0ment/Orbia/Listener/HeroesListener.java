package me.G4meM0ment.Orbia.Listener;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.CharacterDamageManager.ProjectileType;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class HeroesListener implements Listener{
	
	private RPGEssentials plugin;
	
	private static HashMap<Projectile, Double> arrowVel = new HashMap<Projectile, Double>();
	
	public HeroesListener(RPGEssentials plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player)) return;
		if(!(event.getEntity() instanceof Arrow)) return;
		
		arrowVel.put(event.getEntity(), event.getEntity().getVelocity().length());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onWeaponDamage(WeaponDamageEvent event) 
	{
		Player p = null;
		if(event.getAttackerEntity() instanceof Player)
			p = (Player) event.getAttackerEntity();
		
		if(event.isProjectile())
			if(event.getAttackerEntity() instanceof Projectile)
				if(((Projectile) event.getAttackerEntity()).getShooter() instanceof Player)
				{
					Projectile arrow = (Projectile) event.getAttackerEntity();
					p = (Player) ((Projectile)event.getAttackerEntity()).getShooter();
					HeroClass hc = plugin.getHeroes().getCharacterManager().getHero(p).getHeroClass();
					double baseclassdmg = hc.getProjectileDamage(ProjectileType.ARROW);
					double leveldmg = (hc.getProjDamageLevel(ProjectileType.ARROW)*plugin.getHeroes().getCharacterManager().getHero(p).getLevel());
					if(arrowVel.containsKey(arrow))
					{
						event.setDamage(calculateVelDmg(arrowVel.get(arrow), baseclassdmg, leveldmg));
						arrowVel.remove(arrow);
					}
					else
						event.setDamage(baseclassdmg+leveldmg);
					
					if(p.getLocation().distance(event.getEntity().getLocation()) > 32 && hc.toString().equalsIgnoreCase("bogenschuetze"))
						event.setDamage(1.0);
					else if(p.getLocation().distance(event.getEntity().getLocation()) > 32 && !hc.toString().equalsIgnoreCase("bogenschuetze"))
						event.setDamage(.0);
				}
	}
	
	/*@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onManaChange(ManaChangeEvent event) 
	{
		manaHandler.updateManaBar(event.getHero().getPlayer(), event.getHero());
	}*/
	
	private double calculateVelDmg(double vel, double basedmg, double leveldmg)
	{
		double dmg = basedmg+leveldmg;
		if(vel >= 2.9)
			return dmg+2.0;
		if(vel >= 1.9)
			return dmg+1.0;
		if(vel >= 0.9)
			return dmg+0.0;
		if(vel >= 0.4)
			return leveldmg+3.0;
		if(vel < 0.4)
			return leveldmg+1.0;
		else
			return 0.0;
	}

}

