package me.G4meM0ment.Chaintrain.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.Chaintrain.Chaintrain;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerHandler {
	
	private Chaintrain subplugin;
	
	private static HashMap<String, String> chained = new HashMap<String, String>();
	
	public PlayerHandler(Chaintrain subplugin) {
		this.subplugin = subplugin;
	}
	public PlayerHandler() {
		subplugin = new Chaintrain();
	}
	
	/**
	 * Chained list getter & setter
	 * @return
	 */
	public HashMap<String, String> getChainedList() {
		return chained;
	}
	public void setChainedList(HashMap<String, String> chained) {
		PlayerHandler.chained = chained;
	}
	
	/**
	 * Chained check
	 * @param playerName
	 * @return
	 */
	public boolean isChained(String playerName) {
		return getChainedList().containsKey(playerName);
	}
	public boolean isChained(Player player) {
		return getChainedList().containsKey(player.getName());
	}
	
	/**
	 * Set player chained
	 * @param chained
	 * @param chainer
	 */
	public void setChained(Player chained, Player chainer) {
		getChainedList().put(chained.getName(), chainer.getName());
	}
	public void setChained(String chainedName, String chainerName) {
		getChainedList().put(chainedName, chainerName);
	}
	
	/**
	 * Set player unchained
	 * @param player
	 */
	public void setUnchained(Player player) {
		getChainedList().remove(player.getName());
	}
	public void setUnchained(String playerName) {
		getChainedList().remove(playerName);
	}
	
	public String getChainer(String chained) {
		return getChainedList().get(chained);
	}
	
	/**
	 * Execute tasks to chain
	 * @param chained
	 * @param chainer
	 */
	public void chain(Player chained, Player chainer) {
		String chainerName = chainer != null ? chainer.getName() : "";
		setChained(chained.getName(), chainerName);
	}
	public void chain(String chainedName, String chainerName) {
		setChained(chainedName, chainerName);
	}
	
	/**
	 * Execute tasks to unchain
	 * @param chained
	 * @param chainer
	 */
	public void unchain(Player chained, Player chainer) {
		//String chainerName = chainer != null ? chainer.getName() : "";
		setUnchained(chained.getName());
	}
	public void unchain(String chainedName, String chainerName) {
		setUnchained(chainedName);
	}
	
	/**
	 * Chain all players
	 * @param all
	 * @param chainer
	 */
	public void chainAll(Player[] all, Player chainer) {
		for(Player p : all) {
			chain(p, chainer);
		}
	}
	public void unchainAll(Player[] all, Player chainer) {
		for(Player p : all) {
			unchain(p, chainer);
		}
	}
	public void unchainAll(Player chainer) {
		for(String playerName : getChainedList().keySet()) {
			unchain(playerName, chainer.getName());
		}
	}
	
	private static List<String> chaining = new ArrayList<String>();
	/**
	 * 
	 * @param chainer
	 * @param chained
	 */
	public void startChaining( final Player chained, final Player chainer) {
		final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(subplugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(chaining.contains(chainer.getName())) {
					if(chained.getLocation().distance(chainer.getLocation()) > 3) {
						chaining.remove(chainer.getName());
					}
					Messenger.sendNotification(chainer, "Fesseln", Material.STRING, "...");
					Messenger.sendNotification(chained, "Fesseln", Material.STRING, "...");
				}
			}
		}, 0, 20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(id);
				
				if(chaining.contains(chainer.getName())) {
					if(isChained(chained)) {
						unchain(chained, chainer);
						addChainItem(chainer);
						Messenger.sendMessage(chained, ChatColor.GRAY+"Du wurdest von %chainer% befreit!", "%chainer%", chainer.getName());
						Messenger.sendMessage(chainer, ChatColor.GRAY+"Du hast %chained% befreit!", "%chained%", chained.getName());
					} else {
						chain(chained, chainer);
						removeChainItem(chainer);
						Messenger.sendMessage(chained, ChatColor.GRAY+"Du wurdest von %chainer% gefesselt!", "%chainer%", chainer.getName());
						Messenger.sendMessage(chainer, ChatColor.GRAY+"Du hast %chained% gefesselt!", "%chained%", chained.getName());
					}
				}
			}
		}, 60);
	}
	
	
	private void addChainItem(Player player) {
		player.getInventory().addItem(new ItemStack(Material.STRING, 1));
	}
	private void removeChainItem(Player player) {
		player.getInventory().removeItem(new ItemStack(Material.STRING, 1));
	}
}
