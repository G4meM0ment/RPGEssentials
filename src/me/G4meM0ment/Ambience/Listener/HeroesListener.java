package me.G4meM0ment.Ambience.Listener;

import me.G4meM0ment.Ambience.Handler.CacheHandler;
import me.G4meM0ment.Ambience.Handler.SoundHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.SpoutManager;

import com.herocraftonline.heroes.api.events.HeroEnterCombatEvent;
import com.herocraftonline.heroes.api.events.HeroLeaveCombatEvent;

public class HeroesListener implements Listener{
	
	private SoundHandler sh;
	private CacheHandler ch;
	
	public HeroesListener(RPGEssentials plugin)
	{
		sh = new SoundHandler(plugin);
		ch = new CacheHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEnterCombat(HeroEnterCombatEvent event)
	{
		Player p = event.getHero().getPlayer();
		if(p == null) return;	
		
		if(!sh.hasCombatMusic(p))
			sh.prePlay(ch.getCombatSound(), SpoutManager.getPlayer(p));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onLeaveCombat(HeroLeaveCombatEvent event)
	{
		Player p = event.getHero().getPlayer();
		if(p == null) return;
		
		if(sh.hasCombatMusic(p))
			sh.prePlay("silence", SpoutManager.getPlayer(p));
	}
}
