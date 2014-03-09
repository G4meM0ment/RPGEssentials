package me.G4meM0ment.Orbia.Listener;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.CharacterDamageManager.ProjectileType;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import com.herocraftonline.heroes.characters.classes.HeroClass.ExperienceType;

public class HeroesListener implements Listener{
	
	private RPGEssentials plugin;
	
	private int maxPvPGainKills = 5;
	private int noPvPGainTime = 7200000;
	private static HashMap<Projectile, Double> arrowVel = new HashMap<Projectile, Double>();
	private static HashMap<String, HashMap<String, Integer>> gainedPvPExp = new HashMap<String, HashMap<String, Integer>>();
	private static HashMap<String, HashMap<String, Long>> noPvPGain = new HashMap<String, HashMap<String, Long>>();
	private static HashMap<String, String> killedLast = new HashMap<String, String>();
	
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
	public void onExperienceChange(ExperienceChangeEvent event) 
	{	
		//if player gained exp from pvp
		if(event.getSource().equals(ExperienceType.PVP)) {
			//player is listed with victims he's not allowed to gain exp from
			if(noPvPGain.containsKey(event.getHero().getPlayer().getName())) {
				//the victim he killed last is in this list => no exp + msg
				if(noPvPGain.get(event.getHero().getPlayer().getName()).containsKey(killedLast.get(event.getHero().getPlayer().getName()))) {
					Messenger.sendMessage(event.getHero().getPlayer(), ChatColor.GRAY+"Du hast diesen Spieler zu oft getötet, du erhälst die nächste Zeit keine Erfahrung für seinen Tod!");
					event.setCancelled(true);
					return;
				}
			}
		}
		
		SpoutPlayer sp = SpoutManager.getPlayer(event.getHero().getPlayer());
		if(event.getExpChange() > 0.0)
			sp.sendNotification("Erfahrung erhalten:", ""+event.getExpChange(), Material.EXP_BOTTLE);
		else
		{
			sp.sendNotification("Erfahrung verloren:", ""+event.getExpChange(), Material.EXP_BOTTLE);
			sp.sendMessage(ChatColor.GRAY+"Erfahrung verloren: "+ChatColor.WHITE+event.getExpChange());
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onHeroKillCharacter(HeroKillCharacterEvent event) {
		if(!(event.getDefender().getEntity() instanceof Player)) return;
		
		Player p = event.getAttacker().getPlayer();
		Player d = (Player) event.getDefender().getEntity();
		
		/*
		 * if player has waited over two hours the map is cleared so he'll gain exp from the ganked again 
		 */
		if(noPvPGain.containsKey(p.getName())) {
			for(String s : noPvPGain.get(p.getName()).keySet()) {
				if(System.currentTimeMillis()-noPvPGain.get(p.getName()).get(s) > noPvPGainTime)
					noPvPGain.get(p.getName()).remove(s);
			}
		}
		
		/*
		 * if player has a map
		 */
		if(gainedPvPExp.containsKey(p.getName())) {
			//he already killed this player
			if(gainedPvPExp.get(p.getName()).containsKey(d.getName())) {
				gainedPvPExp.get(p.getName()).put(d.getName(), gainedPvPExp.get(p.getName()).get(d.getName())+1);
				
				//if he killed the player more than 5 times he won't be able to gain exp from him
				if(gainedPvPExp.get(p.getName()).get(d.getName()) >= maxPvPGainKills) {
					HashMap<String, Long> map = new HashMap<String, Long>();
					map.put(d.getName(), System.currentTimeMillis());
					noPvPGain.put(p.getName(), map);
				}	
			//new map for victim is arranged	
			} else
				gainedPvPExp.get(p.getName()).put(d.getName(), 1);
		/*
		 * new map is arranged
		 */
		} else {
			HashMap<String, Integer> defender = new HashMap<String, Integer>();
			defender.put(d.getName(), 1);
			gainedPvPExp.put(p.getName(), defender);
		}
		
		//tracked who the player killed
		killedLast.put(p.getName(), d.getName());
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

