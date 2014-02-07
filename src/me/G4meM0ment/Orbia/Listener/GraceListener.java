package me.G4meM0ment.Orbia.Listener;

import java.util.HashMap;

import me.G4meM0ment.DeathAndRebirth.Events.PlayerResurrectEvent;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.herocraftonline.heroes.api.events.SkillDamageEvent;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;

public class GraceListener implements Listener {
	
	private static RPGEssentials plugin;
	
	public static HashMap<Player, long[]> gracers = new HashMap<Player, long[]>();
	
	public GraceListener(RPGEssentials plugin)
	{
		GraceListener.plugin = plugin;
	}
	
	public static void addGracer(final Player p, long duration, final String addMessage, final String removeMessage)
	{
		long[] array = new long[2];
		array[0] = duration;
		array[1] = System.currentTimeMillis();
		gracers.put(p, array);
		
		Messenger.sendMessage(p, addMessage);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				gracers.remove(p);
				Messenger.sendMessage(p, removeMessage);
			}
		}, duration/50);
	}
	
	private boolean handleDamage(Player p1, Player p2)
	{
		if(gracers.containsKey(p1))
			Messenger.sendMessage(p1, ChatColor.GRAY+"Du hast noch PvP Schutz für "+ChatColor.WHITE+(gracers.get(p1)[0]-(System.currentTimeMillis()-gracers.get(p1)[1]))/1000+ChatColor.GRAY+" Sekunden");
		if(gracers.containsKey(p2) && !gracers.containsKey(p1))
			Messenger.sendMessage(p1, p2.getName()+ChatColor.GRAY+" hat noch PvP Schutz für "+ChatColor.WHITE+(gracers.get(p2)[0]-(System.currentTimeMillis()-gracers.get(p2)[1]))/1000+ChatColor.GRAY+" Sekunden");
		
		if(gracers.containsKey(p1) || gracers.containsKey(p2))
			return true;
		return false;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerResurrect(PlayerResurrectEvent event)
	{
		if(event.getResurrected() == null) return;
		final Player p = event.getResurrected();
		
		addGracer(p, 30000, ChatColor.GRAY+"Du hast den Schutz des Talgeron für 30 Sekunden!", ChatColor.GRAY+"Der Schutz des Talgeron verfliegt");
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onWeaponDamage(WeaponDamageEvent event)
	{
		if(!(event.getDamager().getEntity() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		Player p1 = (Player) event.getDamager().getEntity();
		Player p2 = (Player) event.getEntity();
		
		if(handleDamage(p1, p2))
			event.setCancelled(true);
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onSkillDamage(SkillDamageEvent event)
	{
		if(!(event.getDamager().getEntity() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		Player p1 = (Player) event.getDamager().getEntity();
		Player p2 = (Player) event.getEntity();	
		
		if(handleDamage(p1, p2))
			event.setCancelled(true);
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onEntityByEntityDamage(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		Player p1 = (Player) event.getDamager();
		Player p2 = (Player) event.getEntity();

		if(handleDamage(p1, p2))
			event.setCancelled(true);
	}
}
