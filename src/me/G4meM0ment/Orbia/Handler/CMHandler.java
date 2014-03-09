package me.G4meM0ment.Orbia.Handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class CMHandler {
	
	private static List<Player> players;
	
	public CMHandler() {
		players  = new ArrayList<Player>();
	}
	
	public boolean isInCombatMode(Player p) {
		return players.contains(p);
	}
	
	public void toggleCombatMode(Player p) {
		if(players.contains(p))
			players.remove(p);
		else
			players.add(p);
	}
}
