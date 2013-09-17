package me.G4meM0ment.Orbia.Listener;

import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private Orbia subplugin;
		
	public PListener(RPGEssentials plugin){
		this.plugin = plugin;
		subplugin = new Orbia();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {		
		if(!(event.getEntity() instanceof Player)) return;
		Player p = event.getEntity();
		if(p.getKiller() == null || !(p.getKiller() instanceof Player)) return;
		Player k = p.getKiller();
		UPlayer up = UPlayer.get(p);
		UPlayer uk = UPlayer.get(k);
		Faction faction = up.getFaction();
		Faction faction2 = uk.getFaction();
		
		if(faction.getRelationTo(faction2).isAtLeast(Rel.ENEMY)) {
			up.setPower(up.getPower() - subplugin.getConfig().getDouble("factionsPowerLossOnEnemyKill"));
			up.sendMessage("You lost "+subplugin.getConfig().getDouble("factionsPowerLossOnEnemyKill")+" power!");
		}
	}

}
