package me.G4meM0ment.Orbia.Listener;

import me.G4meM0ment.Orbia.Handler.Duell.DuellHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuellListener implements Listener {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private DuellHandler dh;
		
	public DuellListener(RPGEssentials plugin){
		this.plugin = plugin;
		dh = new DuellHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event) 
	{		
		Player p = event.getPlayer();
		if(!dh.isInDuell(p, true, false)) return;
		Player p2 = Bukkit.getPlayer(dh.getDuellPartner(p.getName()));
		
		if(p2 != null)
			p2.sendMessage(ChatColor.DARK_RED+"Du hast das Duell gewonnen!");
			
		dh.removeDuell(p.getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		Player p = event.getPlayer();
		if(p == null) return;

		if(!dh.isInDuell(p, true, false)) return;
		Player p2 = Bukkit.getPlayer(dh.getDuellPartner(p.getName()));
		
		if(p2 != null)
			p2.sendMessage(ChatColor.DARK_RED+"Du hast das Duell gewonnen!");
			
		dh.removeDuell(p.getName());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		final Player p = (Player) event.getEntity();
		
		Damageable d = p;
		
		if(!dh.isInDuell(p, true, false)) return;	
		
		final Player p2 = Bukkit.getPlayer(dh.getDuellPartner(p.getName()));
		if(d.getHealth() - event.getDamage() <= 0)
		{
			GraceListener.addGracer(p, 10000, "", "");
			event.setDamage(0.0);
			p.setHealth(p.getMaxHealth()); //old 6.0
			
			p.sendMessage(ChatColor.DARK_RED+"Du hast das Duell verloren!");
			if(p2 != null)
			{
				p2.sendMessage(ChatColor.DARK_RED+"Du hast das Duell gewonnen ("+Math.round(p2.getHealth())+" Lebenspunkte ¸brig)!");
				p2.setHealth(p2.getMaxHealth());
				GraceListener.addGracer(p2, 10000, "", "");
			}
			dh.removeDuell(p.getName());			
		}
		event.setCancelled(false);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		
		if(!dh.isInDuell(p, true, false)) return;
		
		Player p2 = Bukkit.getPlayer(dh.getDuellPartner(p.getName()));
		if(p2 == null)
		{
			p.sendMessage(ChatColor.DARK_RED+"Auﬂerhalb der Duell Reichweite... Duell beendet!");
			dh.removeDuell(p.getName());
			return;
		}
		
		if(p.getLocation().distance(p2.getLocation()) >= 300)
		{
			p.sendMessage(ChatColor.DARK_RED+"Auﬂerhalb der Duell Reichweite... Duell beendet!");
			p2.sendMessage(ChatColor.DARK_RED+"Auﬂerhalb der Duell Reichweite... Duell beendet!");
			dh.removeDuell(p.getName());
		}
	}
}
