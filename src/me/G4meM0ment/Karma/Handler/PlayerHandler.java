package me.G4meM0ment.Karma.Handler;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerHandler {
	
	private static HashMap<String, Integer> karma = new HashMap<String, Integer>();
	
	
	public HashMap<String, Integer> getKarmaList() {
		return karma;
	}
	public void setKarmaList(HashMap<String, Integer> karma) {
		PlayerHandler.karma = karma;
	}
	
	public int getKarma(Player player) {
		return getKarmaList().get(player.getName());
	}
	public int getKarma(String playerName) {
		return getKarmaList().get(playerName);
	}
	
	public void setKarma(String playerName, int karma) {
		getKarmaList().put(playerName, karma);
	}
	public void setKarma(Player player, int karma) {
		getKarmaList().put(player.getName(), karma);
	}
}
