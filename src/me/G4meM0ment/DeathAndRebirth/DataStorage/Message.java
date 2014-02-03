package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Message {
	
	private static DeathAndRebirth subplugin;
	
	private static File file;
	private static FileConfiguration msgs;
	
	/*
	 * 
	 * Settings
	 * 
	 */
	public static String spoutTitle = "Death and Rebirth";
	public static Material spoutMaterial = Material.BONE;
	
	public static String died = "You've died and become a ghost";
	public static String resurrect = "You've been resurrected";
	public static String resurrectedBy = "You've been resurrected by %resurrecter%";
	public static String cantDoThat = "You can't do that while being a ghost";
	public static String soulBound = "Soul bound";
	public static String soulUnbound = "Sould unbound";
	public static String cantResYet = "You need to wait %seconds% until you can resurrect";

	/*
	 * 
	 * Settings paths
	 * 
	 */
	private static String spoutTitlePath = "spoutTitle";
	private static String spoutMaterialPath = "spoutMaterial";
	
	private static String diedPath = "died";
	private static String resurrectPath = "resurrect";
	private static String resurrectedByPath = "resurrectedBy";
	private static String cantDoThatPath = "cantDoThat";
	private static String soulBoundPath = "soulBound";
	private static String soulUnboundPath = "soulUnbound";
	private static String cantResYetPath = "cantResYet";
	
	public Message()
	{
		subplugin = new DeathAndRebirth();
	}
	
	public static void reloadFile() 
	{
	    if (file == null) 
	    {
	    	file = new File(subplugin.getDir()+"/messages.yml");
	    	subplugin.getLogger().info(subplugin.getLogTit()+"Created Config.");
	    }
	    msgs = YamlConfiguration.loadConfiguration(file);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = subplugin.getPlugin().getResource("darMessages.yml");
	    if (defConfigStream != null)
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        msgs.setDefaults(defConfig);
	        msgs.options().copyHeader(true);
	        msgs.options().copyDefaults(true);
	    }
	    subplugin.getLogger().info(subplugin.getLogTit()+"Messages file loaded.");
	}
	public static void saveFile() 
	{
	    if (msgs == null || msgs == null)
	    	return;
	    try 
	    {
	        msgs.save(file);
	    } 
	    catch (IOException ex)
	    {
	    	subplugin.getLogger().log(Level.SEVERE, subplugin.getLogTit()+"Could not save data to " + file, ex);
	    }
	}
	
	public static void loadMessages()
	{		
		spoutTitle = msgs.getString(spoutTitlePath);
		spoutMaterial = Material.getMaterial(msgs.getString(spoutMaterialPath));
		
		died = msgs.getString(diedPath);
		resurrect = msgs.getString(resurrectPath);
		resurrectedBy = msgs.getString(resurrectedByPath);
		cantDoThat = msgs.getString(cantDoThatPath);
		soulBound = msgs.getString(soulBoundPath);
		soulUnbound = msgs.getString(soulUnboundPath);
		cantResYet = msgs.getString(cantResYetPath);
	}
}
