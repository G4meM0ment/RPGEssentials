package me.G4meM0ment.Orbia.Listener;

import java.io.File;

import me.G4meM0ment.Karma.Karma;
import me.G4meM0ment.Orbia.Orbia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.getspout.spoutapi.SpoutManager;

public class TitleListener implements Listener {
	
	@SuppressWarnings("unused")
	private Orbia subplugin;
	
	private FileConfiguration pexConfig;
	
	public TitleListener()
	{
		subplugin = new Orbia();
		File pex = new File("plugins/PermissionsEx/permissions.yml");
		pexConfig = YamlConfiguration.loadConfiguration(pex);
	}
	
	public void startAutoUpdater()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable()
		{
			@Override
			public void run() 
			{
				for(Player p : Bukkit.getOnlinePlayers())
					SpoutManager.getPlayer(p).setTitle(getTitle(p));
			}
		}, 0, 40);
	}
	
	public String getTitle(Player p)
	{
		String standardSuffix = "Fremder Wanderer";
		String title = "";
		
		/*
		 * add karma
		 */
		if(Karma.getPlayerHandler().getKarma(p) < 0)
			title += ChatColor.RED;
		else 
			title += ChatColor.BLUE;
		
		/*
		 * name
		 */
		title += p.getName();
		
		/*
		 * Suffix from pex
		 */
		String suffix = "";
		
		if(!pexConfig.getConfigurationSection("users."+p.getName()).contains("suffix"))
			suffix = standardSuffix;
		else if(pexConfig.getString("users."+p.getName()+".suffix") == null)
			suffix = standardSuffix;
		else if(pexConfig.getString("users."+p.getName()+".suffix").isEmpty())
			suffix = standardSuffix;
		else
			suffix = pexConfig.getString("users."+p.getName()+".suffix");
		
		if(suffix.startsWith(" "))
			suffix = String.valueOf(suffix.subSequence(1, suffix.length()));
		if(Character.isLowerCase(suffix.charAt(0)))
			suffix = suffix.substring(0,1).toUpperCase() + suffix.substring(1);
		
		title += "\n"+ChatColor.GOLD+suffix;
		
		/*
		 * health
		 */
		title += "\n"+ChatColor.WHITE+((int)p.getHealth())+"/"+((int)p.getMaxHealth());
		
		return title;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		SpoutManager.getPlayer(p).setTitle(getTitle(p));
	}
	
	/*@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onWeaponDamage(WeaponDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		SpoutManager.getPlayer(p).setTitle(getTitle(p));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onSkillDamage(SkillDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		SpoutManager.getPlayer(p).setTitle(getTitle(p));
	} */
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		SpoutManager.getPlayer(p).setTitle(getTitle(p));
	}
	
}
