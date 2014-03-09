package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;

public class FoodListener implements Listener {
	
	private RPGEssentials plugin;
	
	private HashMap<Material, Double> food = new HashMap<Material, Double>();
	private List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
	
	public FoodListener(RPGEssentials plugin) {
		this.plugin = plugin;
		
		food.put(Material.APPLE, 2.0);
		food.put(Material.BAKED_POTATO, 3.0);
		food.put(Material.BREAD, 2.5);
		food.put(Material.CARROT_ITEM, 2.0);
		food.put(Material.COOKED_BEEF, 4.0);
		food.put(Material.COOKED_CHICKEN, 3.0);
		food.put(Material.COOKED_FISH, 2.5);
		food.put(Material.GRILLED_PORK, 4.0);
		food.put(Material.PORK, 1.5);
		food.put(Material.POTATO_ITEM, 0.5);
		food.put(Material.RAW_BEEF, 1.5);
		food.put(Material.RAW_CHICKEN, 1.0);
		food.put(Material.RAW_FISH, 0.5);
		food.put(Material.ROTTEN_FLESH, 0.5); //normal 1.0 need to think about for hunger potion effect
		food.put(Material.POISONOUS_POTATO, 0.2);
		food.put(Material.MELON_STEM, 1.0);
		food.put(Material.MUSHROOM_SOUP, 3.0);
		//TODO hunger effect
		//TODO add cake
		
		effects.add(PotionEffectType.HARM);
		effects.add(PotionEffectType.HUNGER);
		effects.add(PotionEffectType.POISON);
		effects.add(PotionEffectType.WEAKNESS);
		effects.add(PotionEffectType.WITHER);
		
		initAutoRegeneration();
	}
	
	private void initAutoRegeneration() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getGameMode().equals(GameMode.CREATIVE)) continue;
					if(!plugin.getHeroes().getCharacterManager().getHero(p).isInCombat() && !hasAnyNegativePotionEffect(p)) {
						double add = 0.5;
						double health = p.getHealth();
						double maxHealth = p.getMaxHealth();
						
						if(health+add > maxHealth)
							p.setHealth(maxHealth);
						else if(health+add < 0.0)
							p.setHealth(0.0);
						else
							p.setHealth(health+add);
					}
				}
			}
		}, 0, 200);
	}
	private boolean hasAnyNegativePotionEffect(Player p) {
		for(PotionEffect pe : p.getActivePotionEffects()) {
			if(effects.contains(pe.getType()))
				return true;
		}
		return false;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		SpoutManager.getPlayer(event.getPlayer()).getMainScreen().getHungerBar().setVisible(false);
		event.getPlayer().setFoodLevel(19);
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		SpoutManager.getPlayer(event.getPlayer()).getMainScreen().getHungerBar().setVisible(false);
		event.getPlayer().setFoodLevel(19);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		
		SpoutManager.getPlayer((Player) event.getEntity()).getMainScreen().getHungerBar().setVisible(false);
		((Player) event.getEntity()).setFoodLevel(19);
		event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		
		if(event.getRegainReason().equals(RegainReason.SATIATED) || event.getRegainReason().equals(RegainReason.REGEN))
			event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if(food.keySet().contains(event.getItem().getType())) {
			Player p = event.getPlayer();
			double add = food.get(event.getItem().getType());
			double health = p.getHealth();
			double maxHealth = p.getMaxHealth();
			
			if(health+add > maxHealth)
				p.setHealth(maxHealth);
			else if(health+add < 0.0)
				p.setHealth(0.0);
			else
				p.setHealth(health+add);
		}
	}
}
