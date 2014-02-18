package me.G4meM0ment.Karma.Listener;

import me.G4meM0ment.Karma.Karma;
import me.G4meM0ment.Karma.Handler.PlayerHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;

public class HListener implements Listener {
	
	private PlayerHandler pH;
	
	public HListener() {
		pH = new PlayerHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!pH.getKarmaList().containsKey(event.getPlayer().getName()))
			pH.setKarma(event.getPlayer(), 50);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onHeroKillCharacter(HeroKillCharacterEvent event) {
		if(!(event.getDefender().getEntity() instanceof Player)) return;
		
		int karmaMod = pH.getKarma((Player) event.getDefender().getEntity()) > 0 ? Karma.karmaKilledGood : Karma.karmaKilledBad;
		pH.setKarma(event.getAttacker().getPlayer(), pH.getKarma(event.getAttacker().getPlayer())+karmaMod);
	}

}
