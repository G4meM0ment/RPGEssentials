package me.G4meM0ment.Junkie.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PListener implements Listener {
	
	private RPGEssentials plugin;
	private Junkie junkie;
		
	public PListener(RPGEssentials plugin){
		this.plugin = plugin;
		junkie = new Junkie();
	}
		


	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		
		Player p = event.getPlayer();
		List<Integer> drugs = new ArrayList<Integer>();
//		drugs = junkie.getConfig().getIntegerList("drugIDs");
		drugs.add(353);
		
		if(drugs.contains(p.getItemInHand().getTypeId())) {
			if(p.getName().equalsIgnoreCase("cypric"))
				p.sendMessage("Jim stinkt nach Drogen... du jetzt auch!");
			else
				p.sendMessage("Oger stinkt nach Drogen... du jetzt auch!");
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
		}
	}
}
