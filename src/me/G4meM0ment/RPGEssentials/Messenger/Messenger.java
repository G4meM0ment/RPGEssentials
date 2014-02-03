package me.G4meM0ment.RPGEssentials.Messenger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Messenger {
	
	/**
	 * Checks for spoutcraftclient enabled and message shorter than 26 chars to match notifications
	 * @param sp
	 * @param msg
	 * @return
	 */
	public static boolean checkSpoutcraftMessage(Player p, String msg)
	{
		if(!((RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials")).isSpoutcraftPluginEnabled())
			return false;
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		if(!sp.isSpoutCraftEnabled() || msg.length() > 26)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param reciever
	 * @param msg
	 */
	public static void sendMessage(Player reciever, String msg)
	{
		if(reciever == null || msg == null) return;
		
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		
		if(msg.isEmpty()) return;
		reciever.sendMessage(msg);
	}
	/**
	 * 
	 * @param p
	 * @param msg
	 * @param placeHolder
	 * @param replacemnet
	 */
	public static void sendMessage(Player reciever, String msg, String placeHolder, String replacement)
	{
		if(reciever == null || msg == null) return;
		
		msg = ChatColor.translateAlternateColorCodes('&', msg.replace(placeHolder, replacement));
		
		if(msg.isEmpty()) return;
		reciever.sendMessage(msg);
	}
	
	/**
	 * 
	 * @param reciever
	 * @param title
	 * @param mat
	 * @param msg
	 */
	public static void sendNotification(Player reciever, String title, Material mat, String msg)
	{
		if(reciever == null || msg == null) return;
		
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if(!checkSpoutcraftMessage(reciever, msg)) 
			sendMessage(reciever, msg);
		else
		{
			SpoutPlayer sp = SpoutManager.getPlayer(reciever);
			sp.sendNotification(title, msg, mat);
		}
	}
	/**
	 * 
	 * @param reciver
	 * @param title
	 * @param mat
	 * @param msg
	 * @param placeHolder
	 * @param replacement
	 */
	public static void sendNotification(Player reciever, String title, Material mat, String msg, String placeHolder, String replacement)
	{
		if(reciever == null || msg == null) return;
		
		msg = ChatColor.translateAlternateColorCodes('&', msg.replace(placeHolder, replacement));
		if(msg.isEmpty()) return;
		
		if(!checkSpoutcraftMessage(reciever, msg)) 
			sendMessage(reciever, msg);
		else
		{
			SpoutPlayer sp = SpoutManager.getPlayer(reciever);
			sp.sendNotification(title, msg, mat);
		}
	}
	/**
	 * 
	 * @param reciver
	 * @param title
	 * @param mat
	 * @param msg
	 * @param placeHolder
	 * @param replacement
	 * @param placeHolder2
	 * @param replacement2
	 */
	public static void sendNotification(Player reciever, String title, Material mat, String msg, String placeHolder, String replacement, String placeHolder2, String replacement2)
	{
		if(reciever == null || msg == null) return;
		
		msg = ChatColor.translateAlternateColorCodes('&', msg.replace(placeHolder, replacement).replace(placeHolder2, replacement2));
		if(msg.isEmpty()) return;
		
		if(!checkSpoutcraftMessage(reciever, msg))
			sendMessage(reciever, msg);
		else
		{
			SpoutPlayer sp = SpoutManager.getPlayer(reciever);
			sp.sendNotification(title, msg, mat);
		}
	}
}
