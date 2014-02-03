package me.G4meM0ment.RPGEssentials.Messenger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Messenger {
	
	/**
	 * Checks for spoutcraftclient enabled and message shorter than 26 chars to match notifications
	 * @param sp
	 * @param msg
	 * @return
	 */
	public static boolean checkSpoutcraftMessage(SpoutPlayer sp, String msg)
	{
		if(sp.isSpoutCraftEnabled() || msg.length() > 26)
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
		if(reciever == null) return;
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
		if(reciever == null) return;
		reciever.sendMessage(msg.replace(placeHolder, replacement));
	}
	
	/**
	 * 
	 * @param reciever
	 * @param title
	 * @param mat
	 * @param msg
	 */
	public static void sendNotification(SpoutPlayer reciever, String title, Material mat, String msg)
	{
		if(reciever == null) return;
		if(!checkSpoutcraftMessage(reciever, msg)) 
			sendMessage(reciever, msg);
		else
			reciever.sendNotification(title, msg, mat);
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
	public static void sendNotification(SpoutPlayer reciever, String title, Material mat, String msg, String placeHolder, String replacement)
	{
		if(reciever == null) return;
		msg = msg.replace(placeHolder, replacement);
		if(!checkSpoutcraftMessage(reciever, msg)) 
			sendMessage(reciever, msg);
		else
			reciever.sendNotification(title, msg, mat);
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
	public static void sendNotification(SpoutPlayer reciever, String title, Material mat, String msg, String placeHolder, String replacement, String placeHolder2, String replacement2)
	{
		if(reciever == null) return;
		msg = msg.replace(placeHolder, replacement).replace(placeHolder2, replacement2);
		if(!checkSpoutcraftMessage(reciever, msg))
			sendMessage(reciever, msg);
		else
			reciever.sendNotification(title, msg, mat);
	}
}
